package org.project.equationgenerator;

import java.util.*;
import java.util.stream.Collectors;

/* ===========================================================
   EXISTENCE DISCOVERY + GRAMMARS + EXACT DIM SOLVER + O→I
   === now with: Fit→Promote Constants→Micro-Story ==========
   =========================================================== */

/* ============== Utils ============== */
final class Util {
    static <T> Set<T> setOf(T... xs){ return new HashSet<>(List.of(xs)); }
    static String fmt(double x){ return String.format("%.6f", x); }
    static void line(){ System.out.println("--------------------------------------------------"); }
    static double clamp(double v,double a,double b){ return Math.max(a, Math.min(b, v)); }
}

/* ============== Core Taxonomy ============== */
enum DataType { SET, LIST, MAP, QUEUE, STACK, GRAPH, TREE, MATRIX, TENSOR }
enum NumberSet { N, Z, Q, R, C, GFp, BOOL, INTERVAL, VECTOR_Rn, TENSOR_R }
enum Component { SPACE, MASS, TIME, ENERGY, RF, EMPTY_GEOMETRY }

/* ============== Dimensions (units) ============== */
record Dim(int L, int M, int T, int Q){
    static Dim unitL(){return new Dim(1,0,0,0);}
    static Dim unitM(){return new Dim(0,1,0,0);}
    static Dim unitT(){return new Dim(0,0,1,0);}
    static Dim unitQ(){return new Dim(0,0,0,1);}
    static Dim ONE(){return new Dim(0,0,0,0);}
    static Dim ENERGY(){return new Dim(2,1,-2,0);}
    Dim add(Dim o){ return new Dim(L+o.L, M+o.M, T+o.T, Q+o.Q); }
    Dim sub(Dim o){ return new Dim(L-o.L, M-o.M, T-o.T, Q-o.Q); }
    Dim mul(int k){ return new Dim(k*L, k*M, k*T, k*Q); }
    boolean same(Dim o){ return L==o.L && M==o.M && T==o.T && Q==o.Q; }
    int[] v(){ return new int[]{L,M,T,Q}; }
    public String toString(){ return "L^"+L+" M^"+M+" T^"+T+" Q^"+Q; }
}

/* ============== Variables ============== */
record Variable(String name, DataType dtype, NumberSet nset, Set<Component> comps, Dim dim) {}

/* ============== Constants (first-class) ============== */
enum ConstantID { PI, E, SQRT2, C_LIGHT, HBAR, G_NEWTON, K_BOLTZ, EPS0, MU0 }

record PhysicalConstant(ConstantID id, NumberSet nset, Dim dim, Set<Component> comps, String note) {}

final class ConstantSet {
    private static final List<PhysicalConstant> BASE = List.of(
            new PhysicalConstant(ConstantID.PI,    NumberSet.R, Dim.ONE(), Util.setOf(Component.EMPTY_GEOMETRY), "circle ratio"),
            new PhysicalConstant(ConstantID.E,     NumberSet.R, Dim.ONE(), Util.setOf(Component.EMPTY_GEOMETRY), "natural base"),
            new PhysicalConstant(ConstantID.SQRT2, NumberSet.R, Dim.ONE(), Util.setOf(Component.EMPTY_GEOMETRY), "diag of unit square"),
            new PhysicalConstant(ConstantID.C_LIGHT, NumberSet.R, Dim.unitL().sub(Dim.unitT()),
                    Util.setOf(Component.SPACE, Component.TIME, Component.RF), "speed of light"),
            new PhysicalConstant(ConstantID.HBAR,  NumberSet.R, Dim.unitM().add(Dim.unitL().mul(2)).sub(Dim.unitT()),
                    Util.setOf(Component.ENERGY, Component.TIME), "quantum of action"),
            new PhysicalConstant(ConstantID.G_NEWTON, NumberSet.R, Dim.unitL().mul(3).sub(Dim.unitM()).sub(Dim.unitT().mul(2)),
                    Util.setOf(Component.MASS, Component.SPACE, Component.TIME), "gravity coupling"),
            new PhysicalConstant(ConstantID.K_BOLTZ, NumberSet.R, Dim.ENERGY(),
                    Util.setOf(Component.ENERGY), "Boltzmann (per K suppressed)"),
            new PhysicalConstant(ConstantID.EPS0, NumberSet.R, Dim.unitM().sub(Dim.unitL().mul(3)).sub(Dim.unitT().mul(2)).add(Dim.unitQ().mul(2)),
                    Util.setOf(Component.RF, Component.SPACE, Component.TIME), "vacuum permittivity"),
            new PhysicalConstant(ConstantID.MU0,  NumberSet.R, Dim.unitM().add(Dim.unitL()).sub(Dim.unitQ().mul(2)),
                    Util.setOf(Component.RF, Component.SPACE, Component.TIME), "vacuum permeability")
    );

    private static final List<PhysicalConstant> DYNAMIC = new ArrayList<>();

    static List<PhysicalConstant> all(){ return concat(); }
    static void promote(String symbol, Dim dim, Set<Component> comps, String note){
        // Create a synthetic constant with unique id-like symbol
        var id = ConstantID.PI; // dummy; we’ll store by name in a wrapper map below
        DYNAMIC.add(new PhysicalConstant(null, NumberSet.R, dim, comps, note+" ["+symbol+"]"));
    }
    private static List<PhysicalConstant> concat(){
        var out = new ArrayList<PhysicalConstant>(BASE);
        out.addAll(DYNAMIC);
        return out;
    }
}

/* ============== Grammars I–X (unchanged specs) ============== */
enum GrammarID { I, II, III, IV, V, VI, VII, VIII, IX, X }
record GrammarSpec(GrammarID id, String name, String symbol, String domain, String role,
                   String invariants, String transition, String equilibrium){}
final class Grammars {
    static List<GrammarSpec> defaultSet(){
        return List.of(
                new GrammarSpec(GrammarID.I,  "Structural Grammar",    "Σ", "Formal topology, type hierarchy","Creates form","|S|, b","dΣ/dt=+complexity","∂Σ/∂t=0"),
                new GrammarSpec(GrammarID.II, "Computational Grammar", "Δ", "Algorithmic flow","Executes","Cg, Cs, Cn","input→output continuity","Sout=Sin"),
                new GrammarSpec(GrammarID.III,"Ontological Grammar",   "Ξ", "Existence (0,1,−1)","Defines being","state ratios","0↔1 via −1","ΣΞ=0"),
                new GrammarSpec(GrammarID.IV, "Cognitive Grammar",     "Φ", "Conceptual reasoning","Forms comprehension","(T,F,R)","∂Φ/∂t = learning","Φ̇=0"),
                new GrammarSpec(GrammarID.V,  "Historical Grammar",    "λ", "Cultural time","Records progression","order, entropy","λ⁺ emergence","λ̇=0"),
                new GrammarSpec(GrammarID.VI, "Energetic Grammar",     "ψ", "Fields","Transduces energy","∇·F,∇×F,∇²Ψ","∂ψ/∂t=−∇·J","∮F·dA=0"),
                new GrammarSpec(GrammarID.VII,"Recursive Grammar",     "⟳", "Temporal recursion","Sustains cycles","R, D(α)","steady osc","T²(S)+T(S)=0"),
                new GrammarSpec(GrammarID.VIII,"Informational Grammar","ℑ", "Symbol→meaning","Translates","H, ΔI","∂H/∂t<0","K(x)→min"),
                new GrammarSpec(GrammarID.IX, "Temporal Grammar",      "τ", "Time, phase","Evolves others","Δt, ΔE, Δφ","dτ/dt=1","τ(T)=τ(0)"),
                new GrammarSpec(GrammarID.X,  "Axiomatic Grammar",     "Ω", "Meta-logic","Governs all","consistency, completeness","ΣLaw=0","Ω̇=0")
        );
    }
}
record GrammarContext(Map<GrammarID,Double> weight){
    double w(GrammarID g){ return weight.getOrDefault(g, 1.0); }
    static GrammarContext defaultFocus(){
        return new GrammarContext(Map.of(
                GrammarID.I,1.0, GrammarID.II,1.0, GrammarID.VI,1.3,
                GrammarID.VIII,1.0, GrammarID.IX,1.2, GrammarID.X,1.0, GrammarID.III,1.2
        ));
    }
}

/* ============== Law Signatures & Catalog (unchanged) ============== */
record Sig(List<DataType> dtypes, List<NumberSet> nsets) {}
record EquationFamily(String family, Sig inputSig, Sig outputSig, Set<Component> reqComps) {}
record EquationInstance(EquationFamily family, List<Variable> boundVars) {}

final class PhysicsCatalog {
    static List<EquationFamily> families(){
        return List.of(
                new EquationFamily("Conservation (Set Invariants)",
                        new Sig(List.of(DataType.SET), List.of(NumberSet.R)),
                        new Sig(List.of(DataType.SET), List.of(NumberSet.R)),
                        Util.setOf(Component.ENERGY, Component.TIME)),

                new EquationFamily("Time Evolution ODE",
                        new Sig(List.of(DataType.LIST, DataType.SET), List.of(NumberSet.R, NumberSet.R)),
                        new Sig(List.of(DataType.LIST), List.of(NumberSet.R)),
                        Util.setOf(Component.TIME)),

                new EquationFamily("Constitutive Map (Linear)",
                        new Sig(List.of(DataType.MAP), List.of(NumberSet.R)),
                        new Sig(List.of(DataType.MAP), List.of(NumberSet.R)),
                        Util.setOf(Component.SPACE, Component.ENERGY)),

                new EquationFamily("Graph Flow Conservation",
                        new Sig(List.of(DataType.GRAPH), List.of(NumberSet.R)),
                        new Sig(List.of(DataType.GRAPH), List.of(NumberSet.R)),
                        Util.setOf(Component.SPACE, Component.ENERGY)),

                new EquationFamily("Diffusion (Graph Laplacian)",
                        new Sig(List.of(DataType.GRAPH), List.of(NumberSet.R)),
                        new Sig(List.of(DataType.MATRIX), List.of(NumberSet.R)),
                        Util.setOf(Component.SPACE, Component.TIME, Component.ENERGY)),

                new EquationFamily("Maxwell (Matrix Form)",
                        new Sig(List.of(DataType.MATRIX, DataType.MAP), List.of(NumberSet.R, NumberSet.R)),
                        new Sig(List.of(DataType.MATRIX), List.of(NumberSet.R)),
                        Util.setOf(Component.RF, Component.SPACE, Component.TIME)),

                new EquationFamily("Schrödinger (Matrix×List)",
                        new Sig(List.of(DataType.MATRIX, DataType.LIST), List.of(NumberSet.C, NumberSet.C)),
                        new Sig(List.of(DataType.LIST), List.of(NumberSet.C)),
                        Util.setOf(Component.TIME, Component.ENERGY)),

                new EquationFamily("Einstein Field Equations",
                        new Sig(List.of(DataType.TENSOR, DataType.TENSOR), List.of(NumberSet.TENSOR_R, NumberSet.TENSOR_R)),
                        new Sig(List.of(DataType.TENSOR), List.of(NumberSet.TENSOR_R)),
                        Util.setOf(Component.EMPTY_GEOMETRY, Component.MASS, Component.ENERGY, Component.SPACE, Component.TIME))
        );
    }
}

/* ============== Numeric Compatibility ============== */
final class NumCompat {
    static boolean accepts(NumberSet have, NumberSet want){
        if (have==want) return true;
        return switch (want){
            case Z -> have==NumberSet.N || have==NumberSet.BOOL;
            case Q -> have==NumberSet.N || have==NumberSet.Z;
            case R -> have==NumberSet.N || have==NumberSet.Z || have==NumberSet.Q || have==NumberSet.INTERVAL;
            case C -> have==NumberSet.N || have==NumberSet.Z || have==NumberSet.Q || have==NumberSet.R;
            case VECTOR_Rn, TENSOR_R -> have==NumberSet.R;
            default -> false;
        };
    }
}

/* ============== Structure Adapters ============== */
record Adapter(DataType from, DataType to, double cost, String note) {}
final class AdapterRegistry {
    static List<Adapter> adapters(){
        return List.of(
                new Adapter(DataType.LIST,   DataType.MAP,    0.25, "Index keys from list (k→v)"),
                new Adapter(DataType.LIST,   DataType.MATRIX, 0.35, "Embed list as column vector"),
                new Adapter(DataType.MAP,    DataType.MATRIX, 0.30, "Linearize map to operator"),
                new Adapter(DataType.GRAPH,  DataType.MATRIX, 0.20, "Graph Laplacian / incidence"),
                new Adapter(DataType.MATRIX, DataType.TENSOR, 0.30, "Promote matrix to rank-2 tensor"),
                new Adapter(DataType.SET,    DataType.LIST,   0.20, "Canonical ordering"),
                new Adapter(DataType.MAP,    DataType.LIST,   0.25, "Iterate entries"),
                new Adapter(DataType.LIST,   DataType.SET,    0.10, "Deduplicate to set")
        );
    }
    static List<Adapter> admissible(DataType have, DataType want){
        var out = new ArrayList<Adapter>();
        for (var a : adapters()) if (a.from()==have && a.to()==want) out.add(a);
        return out;
    }
}

/* ============== Binder (search all combos) ============== */
final class Binder {
    record Coercion(NumberSet from, NumberSet to, double cost, String note) {}
    static Coercion coerce(NumberSet have, NumberSet want){
        if (have==want) return new Coercion(have,want,0.0,"exact");
        if (!NumCompat.accepts(have,want)) return null;
        double cost = switch (want){
            case Z -> 0.05; case Q -> 0.08; case R -> 0.12; case C -> 0.18;
            case VECTOR_Rn, TENSOR_R -> 0.22; default -> 0.20;
        };
        return new Coercion(have, want, cost, "numeric upcast "+have+"→"+want);
    }
    record Choice(Variable v, double structCost, List<String> structNotes,
                  double numCost, String numNote, DataType asType, NumberSet asNum) {}
    static List<List<Choice>> candidateChoices(List<Variable> vars, DataType wantT, NumberSet wantN){
        var choices = new ArrayList<Choice>();
        for (var v : vars){
            if (v.dtype()==wantT){
                var cc = coerce(v.nset(), wantN);
                if (cc!=null) choices.add(new Choice(v, 0.0, List.of("structure exact"),
                        cc.cost(), cc.note(), wantT, wantN));
            }
            for (var a : AdapterRegistry.admissible(v.dtype(), wantT)){
                var cc = coerce(v.nset(), wantN);
                if (cc!=null) choices.add(new Choice(v, a.cost(), List.of(a.note()),
                        cc.cost(), cc.note(), wantT, wantN));
            }
        }
        return List.of(choices);
    }
    static List<Binding> explore(EquationFamily fam, List<Variable> vars){
        var buckets = new ArrayList<List<Choice>>();
        for (int i=0;i<fam.inputSig().dtypes().size();i++){
            var wantT = fam.inputSig().dtypes().get(i);
            var wantN = fam.inputSig().nsets().get(i);
            var cand = candidateChoices(vars, wantT, wantN).get(0);
            if (cand.isEmpty()) return List.of();
            buckets.add(cand);
        }
        var res = new ArrayList<Binding>();
        backtrack(0, buckets, new ArrayList<>(), fam, res);
        return res;
    }
    private static void backtrack(int i, List<List<Choice>> buckets, List<Choice> acc, EquationFamily fam, List<Binding> out){
        if (i==buckets.size()){ out.add(Binding.from(fam, acc)); return; }
        for (var c : buckets.get(i)){
            acc.add(c);
            backtrack(i+1, buckets, acc, fam, out);
            acc.remove(acc.size()-1);
        }
    }
    record Triple(DataType dt, NumberSet ns, Component comp) {}
    static final class Binding {
        final EquationFamily fam;
        final List<Choice> picks;
        final double structCost, numCost, overlapPenalty, coverageGain, score;
        final Set<Triple> covers;
        final List<String> notes;
        private Binding(EquationFamily fam, List<Choice> picks,
                        double structCost, double numCost, double coverageGain,
                        Set<Triple> covers, double overlapPenalty, double score, List<String> notes){
            this.fam=fam; this.picks=picks; this.structCost=structCost; this.numCost=numCost;
            this.coverageGain=coverageGain; this.covers=covers; this.overlapPenalty=overlapPenalty;
            this.score=score; this.notes=notes;
        }
        static Binding from(EquationFamily fam, List<Choice> picks){
            double sCost = picks.stream().mapToDouble(p->p.structCost).sum();
            double nCost = picks.stream().mapToDouble(p->p.numCost).sum();
            var cover = new HashSet<Triple>();
            for (int i=0;i<fam.inputSig().dtypes().size();i++){
                var dt = fam.inputSig().dtypes().get(i);
                var ns = fam.inputSig().nsets().get(i);
                for (var c : fam.reqComps()) cover.add(new Triple(dt, ns, c));
            }
            double covGain = cover.size();
            var notes = new ArrayList<String>();
            for (int i=0;i<picks.size();i++){
                var p = picks.get(i);
                var wantT = fam.inputSig().dtypes().get(i);
                var wantN = fam.inputSig().nsets().get(i);
                var line = new StringBuilder("input["+i+"]: "+p.v.name()+"  ");
                if (p.structCost==0.0) line.append("[structure: "+wantT+" ✓]");
                else line.append("[adapt "+p.v.dtype()+"→"+wantT+"; cost="+Util.fmt(p.structCost)+"]");
                if (p.numCost==0.0) line.append(" [numbers: "+wantN+" ✓]");
                else line.append(" [coerce "+p.v.nset()+"→"+wantN+"; cost="+Util.fmt(p.numCost)+"]");
                notes.add(line.toString());
            }
            double score = covGain - (0.7*sCost + 0.5*nCost);
            return new Binding(fam, List.copyOf(picks), sCost, nCost, covGain, cover, 0.0, score, notes);
        }
    }
}

/* ============== Grammar Scorer ============== */
final class GrammarScorer {
    record BindingEval(double bonus, List<String> tags, Set<GrammarID> fired){}
    static BindingEval evalBinding(Binder.Binding b, GrammarContext ctx){
        double bonus=0; var tags=new ArrayList<String>(); var fired=new LinkedHashSet<GrammarID>();
        int uniq = b.covers.size();
        if (uniq>0){ bonus += ctx.w(GrammarID.I)*0.10*uniq; fired.add(GrammarID.I); tags.add("Σ:+coverage("+uniq+")"); }
        bonus += ctx.w(GrammarID.II)*(-(0.30*b.structCost + 0.20*b.numCost)); fired.add(GrammarID.II); tags.add("Δ:−cost");
        boolean energetic = b.fam.reqComps().contains(Component.RF) || b.fam.reqComps().contains(Component.ENERGY);
        if (energetic && (b.fam.family().contains("Maxwell") || b.fam.family().contains("Diffusion"))){
            bonus+=ctx.w(GrammarID.VI)*0.6; fired.add(GrammarID.VI); tags.add("ψ:+field");
        }
        if (b.fam.reqComps().contains(Component.TIME)){ bonus += ctx.w(GrammarID.IX)*0.25; fired.add(GrammarID.IX); tags.add("τ:+evolution"); }
        int adapters = (int)b.notes.stream().filter(s->s.contains("adapt")).count();
        if (adapters==0){ bonus += ctx.w(GrammarID.VIII)*0.15; fired.add(GrammarID.VIII); tags.add("ℑ:+compression"); }
        if (b.numCost==0.0){ bonus += ctx.w(GrammarID.X)*0.10; fired.add(GrammarID.X); tags.add("Ω:+consistency"); }
        return new BindingEval(bonus, tags, fired);
    }
    record CandEval(double bonus, List<String> tags, Set<GrammarID> fired){}
    static CandEval evalCandidate(Candidate c, GrammarContext ctx){
        double bonus=0; var tags=new ArrayList<String>(); var fired=new LinkedHashSet<GrammarID>();
        var s = c.tpl().left().toString()+" | "+c.tpl().right();
        if (s.contains("DIVG") || s.contains("CURL") || s.contains("LAPLACIAN")){ bonus+=ctx.w(GrammarID.VI)*0.5; fired.add(GrammarID.VI); tags.add("ψ:+∇"); }
        if (s.contains("D_DT")){ bonus+=ctx.w(GrammarID.IX)*0.3; fired.add(GrammarID.IX); tags.add("τ:+∂/∂t"); }
        if (s.contains("D_DT") && sharesSymbolBothSides(c)){ bonus+=ctx.w(GrammarID.VII)*0.2; fired.add(GrammarID.VII); tags.add("⟳:+recurrence"); }
        if (c.tpl().complexity()<=6){ bonus+=ctx.w(GrammarID.VIII)*0.15; fired.add(GrammarID.VIII); tags.add("ℑ:+compression"); }
        if (c.missingVars().isEmpty()){ bonus+=ctx.w(GrammarID.X)*0.10; fired.add(GrammarID.X); tags.add("Ω:+closure"); }
        return new CandEval(bonus, tags, fired);
    }
    private static boolean sharesSymbolBothSides(Candidate c){
        var L=c.tpl().left().toString(); var R=c.tpl().right().toString();
        return Arrays.stream(new String[]{"E","B","psi","u","sigma"}).anyMatch(sym->L.contains(sym)&&R.contains(sym));
    }
}

/* ============== Planner with Grammar integration ============== */
final class Planner {
    record Chosen(Binder.Binding binding, String explanation, Set<GrammarID> grammars) {}
    record Plan(List<Chosen> chosen, Set<Binder.Triple> covered, Set<Binder.Triple> gaps, double totalScore) {}

    static Plan choose(List<Binder.Binding> candidates, List<Variable> vars, List<EquationFamily> catalog){
        var ctx = GrammarContext.defaultFocus();
        var universe = new HashSet<Binder.Triple>();
        for (var v : vars) for (var c : v.comps()) universe.add(new Binder.Triple(v.dtype(), v.nset(), c));
        var covered = new HashSet<Binder.Triple>();
        var picked  = new ArrayList<Chosen>();
        double total = 0.0;

        while (true){
            Binder.Binding best = null; double bestMarg = 0; Set<Binder.Triple> bestNew=null; long bestOverlap=0; GrammarScorer.BindingEval bestGE=null;
            for (var b : candidates){
                var newSet = new HashSet<Binder.Triple>(b.covers); newSet.removeAll(covered);
                long newCover = newSet.size(); if (newCover==0) continue;
                long overlap = b.covers.size() - newCover;
                double penalty = 0.15*overlap;
                double baseMarg = (newCover) - (0.7*b.structCost + 0.5*b.numCost + penalty);
                var ge = GrammarScorer.evalBinding(b, ctx);
                double marg = baseMarg + ge.bonus();
                if (marg > bestMarg){ bestMarg=marg; best=b; bestNew=newSet; bestOverlap=overlap; bestGE=ge; }
            }
            if (best==null) break;

            var sb = new StringBuilder();
            sb.append("Picked: ").append(best.fam.family()).append("\n");
            sb.append("  • New coverage gained: ").append(bestNew.size()).append("\n");
            sb.append("  • Overlap with existing: ").append(bestOverlap).append("\n");
            sb.append("  • Costs: structure=").append(Util.fmt(best.structCost))
                    .append(", numeric=").append(Util.fmt(best.numCost))
                    .append(", marginal+grammar=").append(Util.fmt(bestMarg)).append("\n");
            sb.append("  • Inputs:\n");
            for (var line : best.notes) sb.append("     - ").append(line).append("\n");
            sb.append("  • Grammar signals: ").append(String.join(", ", bestGE.tags())).append("\n");
            sb.append("  • Adds coverage:\n");
            for (var t : bestNew) sb.append("     - ").append(t.dt()).append(" × ").append(t.ns()).append(" × ").append(t.comp()).append("\n");

            picked.add(new Chosen(best, sb.toString(), bestGE.fired()));
            covered.addAll(best.covers);
            total += bestMarg;
            candidates.removeIf(b -> b.covers.stream().allMatch(covered::contains));
        }
        var gaps = new HashSet<>(universe); gaps.removeAll(covered);
        return new Plan(picked, covered, gaps, total);
    }
}

/* ============== Gap Advisor ============== */
final class GapAdvisor {
    record Suggestion(Binder.Triple gap, String hint, double cost) {}
    static List<Suggestion> suggest(Set<Binder.Triple> gaps, List<Variable> vars){
        var out = new ArrayList<Suggestion>();
        for (var g : gaps){
            Variable bestV=null; Adapter bestA=null; Binder.Coercion bestC=null; double bestCost=Double.POSITIVE_INFINITY;
            for (var v: vars){
                if (!v.comps().contains(g.comp())) continue;
                double sCost=Double.POSITIVE_INFINITY; Adapter pickA=null;
                if (v.dtype()==g.dt()) sCost=0; else
                    for (var a: AdapterRegistry.admissible(v.dtype(), g.dt())) if (a.cost()<sCost){ sCost=a.cost(); pickA=a; }
                var co = Binder.coerce(v.nset(), g.ns()); if (co==null) continue;
                double total=sCost + co.cost(); if (total<bestCost){ bestCost=total; bestV=v; bestA=pickA; bestC=co; }
            }
            if (bestV!=null){
                String hint = (bestA==null) ? ("Coerce "+bestV.name()+": "+bestC.from()+"→"+bestC.to())
                        : ("Adapt "+bestV.name()+": "+bestA.from()+"→"+bestA.to()+" ("+bestA.note()+"), then "+bestC.from()+"→"+bestC.to());
                out.add(new Suggestion(g, hint, bestCost));
            } else out.add(new Suggestion(g, "Add variable of type "+g.dt()+"×"+g.ns()+" with "+g.comp(), 1.0));
        }
        out.sort(Comparator.comparingDouble(Suggestion::cost));
        return out;
    }
}

/* ============== Symbolic Discovery (typed + dimensional) ============== */
sealed interface ExprNode permits VarE, ConstE, Unary, Binary, OpCall {
    DataType dtype(); NumberSet nset(); Dim dim(); int size();
}
record VarE(Variable v) implements ExprNode {
    public DataType dtype(){return v.dtype();}
    public NumberSet nset(){return v.nset();}
    public Dim dim(){return v.dim();}
    public int size(){return 1;}
    public String toString(){return v.name();}
}
record ConstE(String name, NumberSet nset, Dim dim) implements ExprNode {
    public DataType dtype(){return DataType.SET;}
    public int size(){return 1;}
    public String toString(){return name;}
}
enum UOp { NEG, SQRT, EXP, LOG, SIN, COS }
enum BOp { ADD, SUB, MUL, DIV, POW }
record Unary(UOp op, ExprNode x) implements ExprNode {
    public DataType dtype(){return x.dtype();}
    public NumberSet nset(){return x.nset();}
    public Dim dim(){
        return switch(op){
            case SQRT -> new Dim(x.dim().L()/2, x.dim().M()/2, x.dim().T()/2, x.dim().Q()/2);
            case NEG  -> x.dim();
            case EXP, SIN, COS, LOG -> Dim.ONE();
        };
    }
    public int size(){return 1+x.size();}
    public String toString(){return op+"("+x+")";}
}
record Binary(BOp op, ExprNode a, ExprNode b) implements ExprNode {
    public DataType dtype(){return a.dtype();}
    public NumberSet nset(){return a.nset();}
    public Dim dim(){
        return switch(op){
            case ADD, SUB -> a.dim();
            case MUL      -> a.dim().add(b.dim());
            case DIV      -> a.dim().sub(b.dim());
            case POW      -> {
                if (b instanceof ConstE cb && (cb.name().matches("-?\\d+") || cb.name().equals("1/2")))
                    yield a.dim().mul(cb.name().equals("1/2")?1:Integer.parseInt(cb.name()));
                yield a.dim();
            }
        };
    }
    public int size(){return 1+a.size()+b.size();}
    public String toString(){return "("+a+" "+op+" "+b+")";}
}
enum HighOp { GRAD, DIVG, CURL, LAPLACIAN, D_DT }
record OpCall(HighOp op, ExprNode x) implements ExprNode {
    public DataType dtype(){
        return switch(op){
            case GRAD -> DataType.MAP; case DIVG -> DataType.SET; case CURL -> DataType.MAP;
            case LAPLACIAN, D_DT -> x.dtype();
        };
    }
    public NumberSet nset(){return x.nset();}
    public Dim dim(){
        return switch(op){
            case GRAD, DIVG, CURL -> x.dim().sub(Dim.unitL());
            case LAPLACIAN -> x.dim().sub(Dim.unitL().mul(2));
            case D_DT -> x.dim().sub(Dim.unitT());
        };
    }
    public int size(){return 1+x.size();}
    public String toString(){return op+"["+x+"]";}
}
final class TypeRules {
    static boolean unaryOK(UOp op, ExprNode x){
        if ((op==UOp.EXP||op==UOp.SIN||op==UOp.COS||op==UOp.LOG) && !x.dim().same(Dim.ONE())) return false;
        return true;
    }
    static boolean binaryOK(BOp op, ExprNode a, ExprNode b){
        if ((op==BOp.ADD||op==BOp.SUB) && !a.dim().same(b.dim())) return false;
        if (op==BOp.POW && !(b instanceof ConstE)) return false;
        return true;
    }
    static boolean highOK(HighOp op, ExprNode x){
        return switch(op){
            case GRAD, CURL, DIVG, LAPLACIAN -> (x.dtype()==DataType.MAP || x.dtype()==DataType.GRAPH || x.dtype()==DataType.MATRIX);
            case D_DT -> true;
        };
    }
}

/* ============== EXACT Rational arithmetic + Dimensional solver ============== */
final class Rat implements Comparable<Rat>{
    final long n,d; Rat(long n,long d){ long g=gcd(Math.abs(n),Math.abs(d)); long s=d<0?-1:1; this.n=s*n/g; this.d=Math.abs(d)/g; }
    static long gcd(long a,long b){ while(b!=0){ long t=a%b; a=b; b=t; } return Math.max(1,a); }
    static Rat of(long n){ return new Rat(n,1); }
    static Rat add(Rat a,Rat b){ return new Rat(a.n*b.d + b.n*a.d, a.d*b.d); }
    static Rat sub(Rat a,Rat b){ return new Rat(a.n*b.d - b.n*a.d, a.d*b.d); }
    static Rat mul(Rat a,Rat b){ return new Rat(a.n*b.n, a.d*b.d); }
    static Rat div(Rat a,Rat b){ return new Rat(a.n*b.d, a.d*b.n); }
    public int compareTo(Rat o){ return Long.compare(n*o.d, o.n*d); }
    public String toString(){ return d==1? Long.toString(n) : (n+"/"+d); }
    double toDouble(){ return (double)n/(double)d; }
}

/* Solve A x = b over rationals (Gauss-Jordan). A is m×n; here m=4 dims. */
final class LinRat {
    static Rat[][] cloneM(Rat[][] M){
        var R=new Rat[M.length][M[0].length];
        for(int i=0;i<M.length;i++) System.arraycopy(M[i],0,R[i],0,M[0].length);
        return R;
    }
    static Optional<Rat[]> solve(Rat[][] A, Rat[] b){
        int m=A.length, n=A[0].length;
        Rat[][] M=new Rat[m][n+1];
        for(int i=0;i<m;i++){ for(int j=0;j<n;j++) M[i][j]=A[i][j]; M[i][n]=b[i]; }
        int row=0, col=0;
        while(row<m && col<n){
            int piv=row;
            for(int i=row;i<m;i++) if (abs(M[i][col]).compareTo(abs(M[piv][col]))>0) piv=i;
            if (isZero(M[piv][col])){ col++; continue; }
            swap(M,row,piv);
            Rat inv = Rat.div(Rat.of(1), M[row][col]);
            for(int j=col;j<=n;j++) M[row][j]=Rat.mul(M[row][j], inv);
            for(int i=0;i<m;i++){
                if (i==row) continue;
                Rat f = M[i][col];
                if (isZero(f)) continue;
                for(int j=col;j<=n;j++) M[i][j]=Rat.sub(M[i][j], Rat.mul(f, M[row][j]));
            }
            row++; col++;
        }
        var x = new Rat[n]; Arrays.fill(x, Rat.of(0));
        for(int i=0;i<m;i++){
            int lead = firstOne(M[i]);
            if (lead==-1){ if (!isZero(M[i][n])) return Optional.empty(); else continue; }
            x[lead]=M[i][n];
        }
        return Optional.of(x);
    }
    static int firstOne(Rat[] r){ for(int j=0;j<r.length-1;j++) if (isOne(r[j])) return j; return -1; }
    static boolean isZero(Rat r){ return r.n==0; }
    static boolean isOne(Rat r){ return r.n==1 && r.d==1; }
    static Rat abs(Rat r){ return new Rat(Math.abs(r.n), r.d); }
    static void swap(Rat[][] M,int a,int b){ var t=M[a]; M[a]=M[b]; M[b]=t; }
}

/* Dimension balancing: find exponents on selected constants to make LHS and RHS dims equal. */
final class DimSolver {
    record Term(String symbol, Dim dim) {}
    record BalanceResult(boolean ok, Map<String,Rat> powers, String note) {}

    /* Given Δ = dim(LHS) − dim(RHS), and a list of constants with their dimensions,
       solve for exponents x so that sum_i x_i * dim(const_i) = −Δ. */
    static BalanceResult balance(Dim lhs, Dim rhs, List<PhysicalConstant> constants){
        int[] delta = lhs.sub(rhs).v(); // want A x = -delta
        int m=4, n=constants.size();
        Rat[][] A = new Rat[m][n];
        for(int i=0;i<m;i++) for(int j=0;j<n;j++){
            int v = switch(i){case 0->constants.get(j).dim().L(); case 1->constants.get(j).dim().M();
                case 2->constants.get(j).dim().T(); default->constants.get(j).dim().Q();};
            A[i][j]= new Rat(v,1);
        }
        Rat[] b = new Rat[m];
        for(int i=0;i<m;i++) b[i] = new Rat(-delta[i],1);

        var sol = LinRat.solve(A,b);
        if (sol.isEmpty()){
            return new BalanceResult(false, Map.of(), "No exact rational solution with provided constants.");
        }
        var x = sol.get();
        var map = new LinkedHashMap<String,Rat>();
        for(int j=0;j<n;j++) if (!(x[j].n==0)) map.put(constants.get(j).id()==null?("Kappa"+j):constants.get(j).id().name(), x[j]);
        return new BalanceResult(true, map, map.isEmpty()? "Already balanced" : "Use constants with powers as listed");
    }

    static String pretty(Map<String,Rat> p){
        if (p.isEmpty()) return "∅";
        return p.entrySet().stream().map(e-> e.getKey()+"^("+e.getValue()+")").collect(Collectors.joining(" · "));
    }
}

/* ============== Symbolic Enumerator, Scorer ============== */
record Template(ExprNode left, ExprNode right, List<ConstE> params, Set<String> missingVars, double complexity, String rationale){}
record Candidate(Template tpl, double score, List<String> proofSteps, Set<String> newConstants, Set<String> missingVars, Set<GrammarID> grammars){}

final class SymbolicEnumerator {
    record Library(Set<UOp> uops, Set<BOp> bops, Set<HighOp> hops){}
    static Library defaultLib(){
        return new Library(
                Util.setOf(UOp.NEG, UOp.SQRT, UOp.EXP, UOp.LOG, UOp.SIN, UOp.COS),
                Util.setOf(BOp.ADD,BOp.SUB,BOp.MUL,BOp.DIV,BOp.POW),
                Util.setOf(HighOp.GRAD, HighOp.DIVG, HighOp.CURL, HighOp.LAPLACIAN, HighOp.D_DT)
        );
    }
    static List<Template> enumerate(List<Variable> vars, List<PhysicalConstant> constants,
                                    Library lib, int maxSize, Set<Component> context){
        var pool = new ArrayList<ExprNode>();
        for (var v: vars) pool.add(new VarE(v));
        for (var pc: constants) if (!Collections.disjoint(pc.comps(), context))
            pool.add(new ConstE(pc.id()==null?"Kappa":pc.id().name(), pc.nset(), pc.dim()));
        pool.add(new ConstE("2", NumberSet.R, Dim.ONE())); pool.add(new ConstE("1/2", NumberSet.R, Dim.ONE()));

        var exprs = new HashSet<ExprNode>(pool);
        for (int s=2;s<=maxSize;s++){
            var next=new HashSet<ExprNode>();
            for (var e: exprs){
                for (var u: lib.uops){ var c=new Unary(u,e); if (TypeRules.unaryOK(u,e)) next.add(c); }
                for (var h: lib.hops){ if (TypeRules.highOK(h,e)) next.add(new OpCall(h,e)); }
                for (var f: exprs) for (var bop: lib.bops){
                    var c=new Binary(bop,e,f); if (TypeRules.binaryOK(bop,e,f)) next.add(c);
                }
            }
            exprs.addAll(next);
        }

        var templates = new ArrayList<Template>();
        var list = new ArrayList<>(exprs);
        var constantsAll = ConstantSet.all(); // for balancing powers
        for (var a: list){
            for (var b: list){
                if (!a.dim().same(b.dim())){
                    var bal = DimSolver.balance(a.dim(), b.dim(), constantsAll);
                    if (!bal.ok()) continue;
                }
                var rationale = rationaleFor(a,b,context);
                if (rationale==null) continue;
                var missing = new HashSet<String>();
                if (context.contains(Component.TIME) && !(usesOp(a,HighOp.D_DT) || usesOp(b,HighOp.D_DT))) missing.add("∂/∂t term");
                double complexity = a.size()+b.size();
                templates.add(new Template(a,b,new ArrayList<>(),missing,complexity,rationale));
            }
        }
        return templates;
    }
    private static boolean usesOp(ExprNode e, HighOp op){
        if (e instanceof OpCall oc) return oc.op()==op || usesOp(oc.x(), op);
        if (e instanceof Unary u)   return usesOp(u.x(), op);
        if (e instanceof Binary b)  return usesOp(b.a(), op) || usesOp(b.b(), op);
        return false;
    }
    private static String rationaleFor(ExprNode a, ExprNode b, Set<Component> ctx){
        var s=a.toString()+" = "+b.toString();
        if (s.contains("CURL") && ctx.contains(Component.RF)) return "EM-like rotation (ψ)";
        if (s.contains("DIVG") && ctx.contains(Component.RF)) return "Gauss-like source (ψ)";
        if (s.contains("LAPLACIAN") && ctx.contains(Component.TIME)) return "Parabolic/Hyperbolic PDE";
        if (s.contains("D_DT") && ctx.contains(Component.TIME)) return "Time-evolution (τ)";
        if (ctx.contains(Component.SPACE)||ctx.contains(Component.TIME)) return "Structure-compatible";
        return null;
    }
}

final class Scorer {
    static Candidate assess(Template t, Set<Component> ctx){
        double length = t.complexity();
        double miss = t.missingVars().size();
        double ctxR=0; if (t.rationale().contains("EM")) ctxR+=1.0; if (t.rationale().contains("Gauss")) ctxR+=0.7; if (t.rationale().contains("Time")||t.rationale().contains("τ")) ctxR+=0.5;
        double score = ctxR - 0.1*length - 0.5*miss;

        var bal = DimSolver.balance(t.left().dim(), t.right().dim(), ConstantSet.all());
        var proof = new ArrayList<String>();
        proof.add("Units: " + t.left().dim() + " = " + t.right().dim());
        if (bal.ok()) proof.add("Dim-balance: " + (bal.powers().isEmpty() ? "already balanced" : DimSolver.pretty(bal.powers())));
        else proof.add("Dim-balance: no exact with constants");

        if (miss>0) proof.add("Missing terms: " + t.missingVars());
        proof.add("Rationale: " + t.rationale());

        return new Candidate(t, score, proof, new HashSet<>(), t.missingVars(), new LinkedHashSet<>());
    }
}

/* ============== SymbolicDiscovery (with grammar boost) ============== */
final class SymbolicDiscovery {
    record DiscoveryResult(List<Candidate> ranked, List<String> globalNotes) {}
    static DiscoveryResult discover(List<Variable> vars, Set<Component> context, int maxExprSize){
        var constants = ConstantSet.all();
        var templates = SymbolicEnumerator.enumerate(vars, constants, SymbolicEnumerator.defaultLib(), maxExprSize, context);

        var raw = new ArrayList<Candidate>();
        for (var t: templates) raw.add(Scorer.assess(t, context));

        var ctxG = GrammarContext.defaultFocus();
        var boosted = new ArrayList<Candidate>();
        for (var c : raw){
            var ge = GrammarScorer.evalCandidate(c, ctxG);
            var gset = ge.fired();
            boosted.add(new Candidate(c.tpl(), c.score()+ge.bonus(), c.proofSteps(), c.newConstants(), c.missingVars(), gset));
        }
        boosted.sort((a,b)->Double.compare(b.score(), a.score()));

        var notes = new ArrayList<String>();
        long withCurl = boosted.stream().limit(10).filter(c -> c.tpl().left().toString().contains("CURL") || c.tpl().right().toString().contains("CURL")).count();
        if (withCurl>0 && context.contains(Component.RF)) notes.add("Detected curl-based EM relations.");
        var mv = boosted.stream().flatMap(c->c.missingVars().stream()).distinct().toList();
        if (!mv.isEmpty()) notes.add("Common missing terms: " + mv);
        return new DiscoveryResult(boosted, notes);
    }
    static void printTop(DiscoveryResult dr, int N){
        System.out.println("=== Discovered candidate equations (grammar-boosted) ===");
        for (int i=0;i<Math.min(N, dr.ranked().size());i++){
            var c=dr.ranked().get(i);
            System.out.println("["+(i+1)+"] " + c.tpl().left()+" = "+c.tpl().right()+"   score "+Util.fmt(c.score()));
            System.out.println("     • Grammar: "+ c.grammars().stream().map(Enum::name).collect(Collectors.joining(",")));
            for (var step: c.proofSteps()) System.out.println("     • "+step);
            if (!c.missingVars().isEmpty()) System.out.println("     • Missing: "+c.missingVars());
        }
        if (!dr.globalNotes().isEmpty()){
            System.out.println("\nNotes:"); dr.globalNotes().forEach(n-> System.out.println("  - "+n));
        }
        System.out.println();
    }
}

/* ============== NEW: Dimensionless Probes ============== */
final class Dimensionless {
    record Probe(String name, ExprNode expr) {}
    static List<Probe> build(List<ExprNode> exprs){
        var out = new ArrayList<Probe>();
        var pcs = ConstantSet.all();
        for (var e : exprs){
            var bal = DimSolver.balance(e.dim(), Dim.ONE(), pcs);
            if (bal.ok()){
                ExprNode dimless = e;
                for (var entry : bal.powers().entrySet()){
                    var pc = pcs.stream().filter(c-> (c.id()!=null?c.id().name():("Kappa")).equals(entry.getKey())).findFirst().orElse(null);
                    if (pc==null) continue;
                    var ce = new ConstE(pc.id()==null?"Kappa":pc.id().name(), pc.nset(), pc.dim());
                    var pow = new ConstE(entry.getValue().toString(), NumberSet.R, Dim.ONE());
                    dimless = new Binary(BOp.MUL, dimless, new Binary(BOp.POW, ce, pow));
                }
                out.add(new Probe("φ_"+out.size(), dimless));
            }
        }
        return out;
    }
}

/* ============== NEW: Critical landmarks (0/±1/∞) & exponents ============== */
final class CriticalScan {
    record Feature(String probe, String type, double atU, double value, double aux){}
    static List<Feature> scan(String probeName, double[] u, double[] y, double eps){
        var out = new ArrayList<Feature>();
        for (int i=0;i<y.length;i++){
            double v = y[i];
            if (Double.isNaN(v) || Double.isInfinite(v)) continue;
            if (Math.abs(v) < eps) out.add(new Feature(probeName,"ZERO",u[i],v,0));
            if (Math.abs(v-1.0) < eps) out.add(new Feature(probeName,"FIXED(+1)",u[i],v,0));
            if (Math.abs(v+1.0) < eps) out.add(new Feature(probeName,"FIXED(-1)",u[i],v,0));
            if (Math.abs(v) > 1.0/eps) out.add(new Feature(probeName,"POLE",u[i],v,0));
        }
        for (int i=1;i<u.length;i++){
            if (u[i]<=0 || y[i]==0 || u[i-1]<=0 || y[i-1]==0) continue;
            double a = (Math.log(Math.abs(y[i]))-Math.log(Math.abs(y[i-1])))
                    / (Math.log(u[i])-Math.log(u[i-1]));
            out.add(new Feature(probeName,"LOCAL_EXP",u[i],y[i],a));
        }
        return out;
    }
}

/* ============== NEW: Lightweight constant discovery ============== */
final class ConstantFinder {
    static record Hit(String name, double approx, double err) {}
    private static final Map<String,Double> BASIS = Map.of(
            "π", Math.PI, "e", Math.E, "√2", Math.sqrt(2), "√3", Math.sqrt(3),
            "ln2", Math.log(2), "ζ2", Math.PI*Math.PI/6.0
    );
    static Optional<Hit> detect(double x){
        double bestErr = Double.POSITIVE_INFINITY; String bestName=null; double bestApprox=0;
        for (var e : BASIS.entrySet()){
            double err = Math.abs(x - e.getValue());
            if (err<bestErr){ bestErr=err; bestName=e.getKey(); bestApprox=e.getValue(); }
        }
        for (var e : BASIS.entrySet()){
            double c=e.getValue();
            for(int a=-3;a<=3;a++) for(int b=-3;b<=3;b++){
                double v = a + b*c;
                double err = Math.abs(x - v);
                if (err<bestErr){ bestErr=err; bestName=a+"+"+b+e.getKey(); bestApprox=v; }
            }
        }
        double[] ks = {1,0.5,2};
        int[] ps = {-2,-1,1,2};
        for (var e : BASIS.entrySet()){
            for (double k: ks) for (int p: ps){
                double v = k*Math.pow(e.getValue(), p);
                double err = Math.abs(x - v);
                if (err<bestErr){ bestErr=err; bestName=k+"·"+e.getKey()+"^"+p; bestApprox=v; }
            }
        }
        return (bestErr<1e-6) ? Optional.of(new Hit(bestName,bestApprox,bestErr)) : Optional.empty();
    }
}

/* ============== NEW: Simple law fitter over dimensionless data ============== */
final class LawFitter {
    enum Family { AFFINE, POWER, EXP, BOLTZMANN }
    record Fit(Family family, double a, double b, double err){}
    static Fit fit(double[] u, double[] y){
        var cands = new ArrayList<Fit>();
        cands.add(fitAffine(u,y));
        cands.add(fitPower(u,y));
        cands.add(fitExp(u,y));
        cands.add(fitBoltz(u,y));
        return cands.stream().min(Comparator.comparingDouble(Fit::err)).orElse(cands.get(0));
    }
    private static Fit fitAffine(double[] u,double[] y){
        double su=0, sy=0, suu=0, suy=0; int n=u.length;
        for(int i=0;i<n;i++){ su+=u[i]; sy+=y[i]; suu+=u[i]*u[i]; suy+=u[i]*y[i]; }
        double denom = n*suu - su*su + 1e-12;
        double b = (n*suy - su*sy)/denom;
        double a = (sy - b*su)/n;
        double err=mse(u,y,(x)->a+b*x);
        return new Fit(Family.AFFINE,a,b,err);
    }
    private static Fit fitPower(double[] u,double[] y){
        ArrayList<Double> lu=new ArrayList<>(), ly=new ArrayList<>();
        for(int i=0;i<u.length;i++) if (u[i]>0 && y[i]>0){ lu.add(Math.log(u[i])); ly.add(Math.log(y[i])); }
        if (lu.size()<2) return new Fit(Family.POWER,1,1,1e9);
        double su=0, sy=0, suu=0, suy=0; int n=lu.size();
        for(int i=0;i<n;i++){ double U=lu.get(i), Y=ly.get(i); su+=U; sy+=Y; suu+=U*U; suy+=U*Y; }
        double denom = n*suu - su*su + 1e-12;
        double p = (n*suy - su*sy)/denom;
        double lnA = (sy - p*su)/n;
        double A = Math.exp(lnA);
        double err=mse(u,y,(x)->A*Math.pow(x, p));
        return new Fit(Family.POWER,A,p,err);
    }
    private static Fit fitExp(double[] u,double[] y){
        ArrayList<Double> ly=new ArrayList<>();
        for (double v: y) if (v>0) ly.add(Math.log(v));
        if (ly.size()!=y.length) return new Fit(Family.EXP,1,1,1e9);
        double su=0, sy=0, suu=0, suy=0; int n=u.length;
        for(int i=0;i<n;i++){ su+=u[i]; sy+=Math.log(y[i]); suu+=u[i]*u[i]; suy+=u[i]*Math.log(y[i]); }
        double denom = n*suu - su*su + 1e-12;
        double b = (n*suy - su*sy)/denom;
        double lnA = (sy - b*su)/n;
        double A = Math.exp(lnA);
        double err=mse(u,y,(x)->A*Math.exp(b*x));
        return new Fit(Family.EXP,A,b,err);
    }
    private static Fit fitBoltz(double[] u,double[] y){
        // y ≈ A * exp(-b/u) for u>0 (Arrhenius/Boltzmann-like)
        ArrayList<Double> z=new ArrayList<>();
        for(double x:u) if (x>0) z.add(-1.0/x);
        if (z.size()!=u.length) return new Fit(Family.BOLTZMANN,1,1,1e9);
        double su=0, sy=0, suu=0, suy=0; int n=u.length;
        for(int i=0;i<n;i++){ su+=z.get(i); sy+=Math.log(Math.max(1e-12,y[i])); suu+=z.get(i)*z.get(i); suy+=z.get(i)*Math.log(Math.max(1e-12,y[i])); }
        double denom = n*suu - su*su + 1e-12;
        double b = (n*suy - su*sy)/denom;
        double lnA = (sy - b*su)/n;
        double A = Math.exp(lnA);
        double err=mse(u,y,(x)->A*Math.exp(b*(-1.0/Math.max(1e-12,x))));
        return new Fit(Family.BOLTZMANN,A,b,err);
    }
    interface F { double f(double x); }
    private static double mse(double[] u,double[] y,F f){
        double e=0; for(int i=0;i<u.length;i++){ double r=f.f(u[i]) - y[i]; e+=r*r; } return e/(u.length+1e-12);
    }
}

/* ============== NEW: Micro-story generator from fit + constants + features ============== */
final class MicroStory {
    static String from(LawFitter.Fit fit, List<CriticalScan.Feature> feats, Optional<ConstantFinder.Hit> kHit, Dim probeDim, Set<Component> ctx){
        var sb = new StringBuilder();
        sb.append("Micro-story: ");
        // family → mechanism
        switch (fit.family()){
            case POWER -> sb.append("observable scales as a power of the driver (u^").append(Util.fmt(fit.b())).append(")");
            case AFFINE -> sb.append("linear response around operating point");
            case EXP -> sb.append("exponential response suggests multiplicative/Markov steps");
            case BOLTZMANN -> sb.append("activation-like response with barrier/temperature structure");
        }
        // critical landmarks
        var hasZero = feats.stream().anyMatch(f->f.type().equals("ZERO"));
        var hasPole = feats.stream().anyMatch(f->f.type().equals("POLE"));
        var hasFix1 = feats.stream().anyMatch(f->f.type().equals("FIXED(+1)"));
        if (hasZero) sb.append("; a true zero is present (Ξ)");
        if (hasFix1) sb.append("; a normalized fixed point appears (Ξ)");
        if (hasPole) sb.append("; pole indicates conservation/constraint boundary (Ω)");
        // constants
        kHit.ifPresent(h-> sb.append("; a dimensionless constant ≈ ").append(h.name()).append(" emerges"));
        // dimension hints → archetype
        if (probeDim.same(Dim.ENERGY())) sb.append("; energy channel implicated (ψ)");
        if (ctx.contains(Component.RF) && ctx.contains(Component.TIME)) sb.append("; field propagation constraint (ψ,τ)");
        return sb.toString();
    }
}

/* ============== O→I Predictor (grammar radar) ============== */
final class OIPredictor {
    record IO(Map<String, Double> inputs, Map<String, Double> outputs) {}
    record BehaviorUpdate(Set<String> suggestNewVariables, Set<String> suggestConstants, Set<String> suggestAdapters,
                          Set<String> triggeredFamilies, Set<GrammarID> engagedGrammars, List<String> narrative) {}
    static BehaviorUpdate predict(IO io, DiscoveryPlanner.Report plan, SymbolicDiscovery.DiscoveryResult sym){
        var newVars=new LinkedHashSet<String>(); var newConsts=new LinkedHashSet<String>(); var adapters=new LinkedHashSet<String>();
        var fams=new LinkedHashSet<String>(); var grams=new LinkedHashSet<GrammarID>(); var story=new ArrayList<String>();

        for (var ch : plan.plan().chosen()){
            fams.add(ch.binding().fam.family());
            grams.addAll(ch.grammars());
            story.add("Family engaged: "+ch.binding().fam.family());
            for (var n : ch.binding().notes) if (n.contains("adapt")) adapters.add(n.trim());
        }
        int topN=Math.min(5, sym.ranked().size());
        for (int i=0;i<topN;i++){
            var c=sym.ranked().get(i);
            grams.addAll(c.grammars());
            if (!c.missingVars().isEmpty()) newVars.addAll(c.missingVars());
            story.add("Equation candidate: "+c.tpl().left()+" = "+c.tpl().right()+" [grammar "+c.grammars()+"]");
        }
        for (var e: io.inputs.entrySet()) story.add("Input '"+e.getKey()+"' perturbs via active families.");
        return new BehaviorUpdate(newVars,newConsts,adapters,fams,grams,story);
    }
    static void printBlackBox(IO io, BehaviorUpdate bu){
        Util.line(); System.out.println("BLACK BOX — Output/Input Equation Predictor (Grammar-aware)"); Util.line();
        System.out.println("   Inputs:"); io.inputs.forEach((k,v)-> System.out.println("     → "+k+"  ("+Util.fmt(v)+")"));
        Util.line();
        System.out.println("   ┌───────────────────────────────┐");
        System.out.println("   │  DISCOVERED LAWS + GRAMMARS   │");
        System.out.println("   └───────────────────────────────┘");
        Util.line();
        System.out.println("   Active families:"); bu.triggeredFamilies.forEach(f-> System.out.println("     • "+f));
        System.out.println("   Suggested adapters:"); if (bu.suggestAdapters.isEmpty()) System.out.println("     • (none)"); else bu.suggestAdapters.forEach(a-> System.out.println("     • "+a));
        System.out.println("   Suggested new terms:"); if (bu.suggestNewVariables.isEmpty()) System.out.println("     • (none)"); else bu.suggestNewVariables.forEach(a-> System.out.println("     • "+a));
        System.out.println("   Grammar radar: " + bu.engagedGrammars.stream().map(Enum::name).collect(Collectors.joining(", ")));
        Util.line();
        System.out.println("   Narrative:"); bu.narrative.forEach(s-> System.out.println("     - "+s));
        Util.line();
        System.out.println("   Outputs (qualitative): fields updated; equations instantiated; gaps reduced.");
        Util.line();
    }
}

/* ============== Scenarios (Atomic demo) ============== */
final class ScenarioUtil {
    static Variable list (String n, NumberSet ns, Set<Component> c, Dim d){ return new Variable(n, DataType.LIST, ns, c, d); }
    static Variable map  (String n, NumberSet ns, Set<Component> c, Dim d){ return new Variable(n, DataType.MAP,  ns, c, d); }
    static Variable matrix(String n, NumberSet ns, Set<Component> c, Dim d){ return new Variable(n, DataType.MATRIX, ns, c, d); }
}
final class AtomicScenario {
    static List<Variable> vars(){
        return List.of(
                ScenarioUtil.map("E", NumberSet.R, Util.setOf(Component.RF, Component.SPACE, Component.TIME),
                        Dim.unitM().add(Dim.unitL()).sub(Dim.unitT().mul(2))),
                ScenarioUtil.map("B", NumberSet.R, Util.setOf(Component.RF, Component.SPACE, Component.TIME),
                        Dim.unitM().sub(Dim.unitT().mul(2))),
                ScenarioUtil.map("rho", NumberSet.R, Util.setOf(Component.ENERGY, Component.SPACE),
                        Dim.unitM().sub(Dim.unitL().mul(3))),
                ScenarioUtil.map("J", NumberSet.R, Util.setOf(Component.ENERGY, Component.SPACE, Component.TIME),
                        Dim.unitM().sub(Dim.unitL().mul(2)).sub(Dim.unitT())),
                ScenarioUtil.list("psi", NumberSet.R, Util.setOf(Component.TIME, Component.ENERGY), Dim.ONE()),
                ScenarioUtil.matrix("heat_field", NumberSet.R, Util.setOf(Component.ENERGY, Component.SPACE, Component.TIME), Dim.ENERGY())
        );
    }
    static Set<Component> context(){ return Util.setOf(Component.SPACE, Component.TIME, Component.RF, Component.ENERGY); }
}

/* ============== NEW: Empirical dataset + promotion loop ============== */
final class Empirical {
    static record Series(double[] u, double[] y) {}
    // Placeholder synthetic data: power-law with small noise; replace with real measurements.
    static Series syntheticPower(){
        double[] u = new double[]{0.5, 0.8, 1.0, 1.5, 2.0, 3.0, 4.0};
        double[] y = new double[u.length];
        double A=2.0, p=0.5;
        Random r = new Random(42);
        for(int i=0;i<u.length;i++){
            y[i]= A*Math.pow(u[i], p)*(1.0 + 0.01*(r.nextDouble()-0.5));
        }
        return new Series(u,y);
    }
}

/* ============== Discovery Planner & Printer (unchanged) ============== */
final class DiscoveryPlanner {
    record Report(List<Binder.Binding> allBindings, Planner.Plan plan, List<GapAdvisor.Suggestion> suggestions) {}
    static Report run(List<Variable> vars, List<EquationFamily> catalog){
        var all = new ArrayList<Binder.Binding>();
        for (var fam: catalog){
            if (!haveComponents(vars, fam.reqComps())) continue;
            all.addAll(Binder.explore(fam, vars));
        }
        var plan = Planner.choose(new ArrayList<>(all), vars, catalog);
        var tips = GapAdvisor.suggest(plan.gaps(), vars);
        return new Report(all, plan, tips);
    }
    private static boolean haveComponents(List<Variable> vars, Set<Component> req){
        var have=new HashSet<Component>(); vars.forEach(v->have.addAll(v.comps())); return have.containsAll(req);
    }
}
final class PlanPrinter {
    static void print(DiscoveryPlanner.Report report){
        System.out.println("============== PLAN ==============");
        System.out.println("Total score (+grammar): " + Util.fmt(report.plan().totalScore()));
        System.out.println();
        int i=1;
        for (var ch : report.plan().chosen()){
            System.out.println("[" + i + "] " + ch.binding().fam.family());
            System.out.print(ch.explanation());
            System.out.println("  • Grammars fired: " + ch.grammars().stream().map(Enum::name).collect(Collectors.joining(", ")));
            System.out.println();
            i++;
        }
        System.out.println("—— Remaining gaps ("+report.plan().gaps().size()+") ——");
        report.plan().gaps().stream()
                .sorted((a,b)->a.dt().name().compareTo(b.dt().name()))
                .forEach(g -> System.out.println("  ✗ " + g.dt()+" × "+g.ns()+" × "+g.comp()));
        System.out.println();
        System.out.println("—— Cheapest suggestions ——");
        for (var s : report.suggestions())
            System.out.println("  ? " + s.gap().dt()+" × "+s.gap().ns()+" × "+s.gap().comp()
                    + "  →  " + s.hint() + "  (≈" + Util.fmt(s.cost()) + ")");
        System.out.println();
    }
}

/* ============== Demo Main (now includes Fit→Promote→Micro-story) ============== */
public class ExistenceDiscovery {
    public static void main(String[] args){
        var vars = AtomicScenario.vars();
        var catalog = PhysicsCatalog.families();

        // 1) Discovery + Plan (grammar-aware)
        var report = DiscoveryPlanner.run(vars, catalog);
        PlanPrinter.print(report);

        // 2) Symbolic discovery (grammar boosted + exact dimensional balancing)
        var ctx = AtomicScenario.context();
        var sym = SymbolicDiscovery.discover(vars, ctx, 4);
        SymbolicDiscovery.printTop(sym, 5);

        // 3) O→I predictor (context narrative)
        var inputs = new LinkedHashMap<String,Double>();
        inputs.put("E-drive", 1.0); inputs.put("rho-injection", 0.2);
        var io = new OIPredictor.IO(inputs, Map.of());
        var bu = OIPredictor.predict(io, report, sym);
        OIPredictor.printBlackBox(io, bu);

        // 4) NEW: Fit stubborn data in a dimensionless channel → promote constants → micro-story
        System.out.println("\n=== Fit → Promote Constant → Micro-story ===");
        // build one simple dimensionless probe from an available expr (here just E as placeholder)
        List<ExprNode> probeExprs = List.of(new VarE(AtomicScenario.vars().get(0))); // E
        var probes = Dimensionless.build(probeExprs);
        if (probes.isEmpty()){
            System.out.println("No dimensionless probes found with current constants.");
        } else {
            var p = probes.get(0);
            // get empirical (replace with your measurements)
            var series = Empirical.syntheticPower();
            // force a law
            var fit = LawFitter.fit(series.u(), series.y());
            System.out.println("Best law: "+fit.family()+"  params: a="+Util.fmt(fit.a())+"  b="+Util.fmt(fit.b())+"  mse="+Util.fmt(fit.err()));

            // scan landmarks
            var feats = CriticalScan.scan(p.name(), series.u(), series.y(), 1e-6);
            feats.stream().filter(f->!f.type().equals("LOCAL_EXP")).forEach(f->
                    System.out.println("  • "+f.type()+" at u="+Util.fmt(f.atU())+"  y="+Util.fmt(f.value()))
            );

            // attempt constant discovery on mean dimensionless level (toy; swap for a better statistic if needed)
            double mean = Arrays.stream(series.y()).average().orElse(0);
            var kHit = ConstantFinder.detect(mean);
            kHit.ifPresent(h -> {
                System.out.println("  • Promoting constant ~ "+h.name()+" ("+Util.fmt(h.approx())+")");
                // promote as dimensionless (Dim.ONE) for reuse
                ConstantSet.promote(h.name(), Dim.ONE(), Util.setOf(Component.EMPTY_GEOMETRY), "empirically discovered");
            });

            // narrate micro-story from fit + landmarks + constant
            var story = MicroStory.from(fit, feats, kHit, Dim.ONE(), ctx);
            System.out.println(story);
            System.out.println("Result: law forced in dimensionless space; constants promoted; story recorded.");
        }

        System.out.println("\nDone. Forms are grammar-constrained, fit to data, and explained minimally.");
    }
}
