import java.util.*;


/* ===========================================================
   EXISTENCE DISCOVERY + O→I PREDICTOR (ALL-IN-ONE)
   - Typed data structures + number sets + components
   - Units/dimensions
   - First-class constants (π, e, √2, c, ħ, G, …)
   - Structure adapters + numeric coercions
   - Exhaustive binding search with scoring + readable proofs
   - Gap analysis + cheapest suggestions
   - Symbolic discovery (typed, dimensional) + rationale
   - Dimensional solver (suggest missing exponents)
   - Scenarios (quark, atomic, material, chemistry)
   - Black-box O→I predictor: outputs feed behavior
   =========================================================== */

// ========================= Utils =========================
final class U {
    @SafeVarargs
    static <T> Set<T> setOf(T... xs){ return new HashSet<>(List.of(xs)); }
    static String fmt(double x){ return String.format("%.2f", x); }
    static void line(){ System.out.println("--------------------------------------------------"); }
}

// ========================= Core Taxonomy =========================
enum DataType { SET, LIST, MAP, QUEUE, STACK, GRAPH, TREE, MATRIX, TENSOR }
enum NumberSet { N, Z, Q, R, C, GFp, BOOL, INTERVAL, VECTOR_Rn, TENSOR_R }
enum Component { SPACE, MASS, TIME, ENERGY, RF, EMPTY_GEOMETRY }

// ========================= Dimensions (units) =========================
record Dim(int L, int M, int T, int Q){ // Length, Mass, Time, Charge-like
    static Dim unitL(){return new Dim(1,0,0,0);}
    static Dim unitM(){return new Dim(0,1,0,0);}
    static Dim unitT(){return new Dim(0,0,1,0);}
    static Dim unitQ(){return new Dim(0,0,0,1);}
    static Dim ONE(){return new Dim(0,0,0,0);}
    static Dim ENERGY(){ return new Dim(2,1,-2,0); } // M L^2 T^-2
    Dim add(Dim o){ return new Dim(L+o.L, M+o.M, T+o.T, Q+o.Q); }
    Dim sub(Dim o){ return new Dim(L-o.L, M-o.M, T-o.T, Q-o.Q); }
    Dim mul(int k){ return new Dim(k*L, k*M, k*T, k*Q); }
    boolean same(Dim o){ return L==o.L && M==o.M && T==o.T && Q==o.Q; }
    public String toString(){ return "L^"+L+" M^"+M+" T^"+T+" Q^"+Q; }
}

// ========================= Variables =========================
record Variable(String name, DataType dtype, NumberSet nset, Set<Component> comps, Dim dim) {}

// ========================= Constants as first-class =========================
enum ConstantID { PI, E, SQRT2, C_LIGHT, HBAR, G_NEWTON, K_BOLTZ, EPS0, MU0 }

record PhysicalConstant(ConstantID id, NumberSet nset, Dim dim, Set<Component> comps, String note) {}

final class ConstantSet {
    static List<PhysicalConstant> all(){
        return List.of(
                new PhysicalConstant(ConstantID.PI,    NumberSet.R, Dim.ONE(),
                        U.setOf(Component.EMPTY_GEOMETRY), "circle ratio; geometric closure"),
                new PhysicalConstant(ConstantID.E,     NumberSet.R, Dim.ONE(),
                        U.setOf(Component.EMPTY_GEOMETRY), "natural growth base"),
                new PhysicalConstant(ConstantID.SQRT2, NumberSet.R, Dim.ONE(),
                        U.setOf(Component.EMPTY_GEOMETRY), "unit square diagonal"),

                new PhysicalConstant(ConstantID.C_LIGHT, NumberSet.R,
                        Dim.unitL().sub(Dim.unitT()), U.setOf(Component.SPACE, Component.TIME, Component.RF),
                        "speed of light links space & time"),

                new PhysicalConstant(ConstantID.HBAR, NumberSet.R,
                        Dim.unitM().add(Dim.unitL().mul(2)).sub(Dim.unitT()), U.setOf(Component.ENERGY, Component.TIME),
                        "quantum of action"),

                new PhysicalConstant(ConstantID.G_NEWTON, NumberSet.R,
                        Dim.unitL().mul(3).sub(Dim.unitM()).sub(Dim.unitT().mul(2)),
                        U.setOf(Component.MASS, Component.SPACE, Component.TIME),
                        "gravitational coupling"),

                new PhysicalConstant(ConstantID.K_BOLTZ, NumberSet.R,
                        Dim.ENERGY(), U.setOf(Component.ENERGY),
                        "entropy/temperature proportionality (per kelvin suppressed here)"),

                new PhysicalConstant(ConstantID.EPS0, NumberSet.R,
                        Dim.unitM().sub(Dim.unitL().mul(3)).sub(Dim.unitT().mul(2)).add(Dim.unitQ().mul(2)),
                        U.setOf(Component.RF, Component.SPACE, Component.TIME),
                        "vacuum permittivity"),

                new PhysicalConstant(ConstantID.MU0, NumberSet.R,
                        Dim.unitM().add(Dim.unitL()).sub(Dim.unitQ().mul(2)),
                        U.setOf(Component.RF, Component.SPACE, Component.TIME),
                        "vacuum permeability")
        );
    }
}

// ========================= Law Signatures & Catalog =========================
record Sig(List<DataType> dtypes, List<NumberSet> nsets) {}

record EquationFamily(String family, Sig inputSig, Sig outputSig, Set<Component> reqComps) {}

record EquationInstance(EquationFamily family, List<Variable> boundVars) {}

// A small catalog (extend as needed)
final class PhysicsCatalog {
    static List<EquationFamily> families(){
        return List.of(
                new EquationFamily("Conservation (Set Invariants)",
                        new Sig(List.of(DataType.SET), List.of(NumberSet.R)),
                        new Sig(List.of(DataType.SET), List.of(NumberSet.R)),
                        U.setOf(Component.ENERGY, Component.TIME)),

                new EquationFamily("Time Evolution ODE",
                        new Sig(List.of(DataType.LIST, DataType.SET), List.of(NumberSet.R, NumberSet.R)),
                        new Sig(List.of(DataType.LIST), List.of(NumberSet.R)),
                        U.setOf(Component.TIME)),

                new EquationFamily("Constitutive Map (Linear)",
                        new Sig(List.of(DataType.MAP), List.of(NumberSet.R)),
                        new Sig(List.of(DataType.MAP), List.of(NumberSet.R)),
                        U.setOf(Component.SPACE, Component.ENERGY)),

                new EquationFamily("Graph Flow Conservation",
                        new Sig(List.of(DataType.GRAPH), List.of(NumberSet.R)),
                        new Sig(List.of(DataType.GRAPH), List.of(NumberSet.R)),
                        U.setOf(Component.SPACE, Component.ENERGY)),

                new EquationFamily("Diffusion (Graph Laplacian)",
                        new Sig(List.of(DataType.GRAPH), List.of(NumberSet.R)),
                        new Sig(List.of(DataType.MATRIX), List.of(NumberSet.R)),
                        U.setOf(Component.SPACE, Component.TIME, Component.ENERGY)),

                new EquationFamily("Maxwell (Matrix Form)",
                        new Sig(List.of(DataType.MATRIX, DataType.MAP), List.of(NumberSet.R, NumberSet.R)),
                        new Sig(List.of(DataType.MATRIX), List.of(NumberSet.R)),
                        U.setOf(Component.RF, Component.SPACE, Component.TIME)),

                new EquationFamily("Schrödinger (Matrix×List)",
                        new Sig(List.of(DataType.MATRIX, DataType.LIST), List.of(NumberSet.C, NumberSet.C)),
                        new Sig(List.of(DataType.LIST), List.of(NumberSet.C)),
                        U.setOf(Component.TIME, Component.ENERGY)),

                new EquationFamily("Einstein Field Equations",
                        new Sig(List.of(DataType.TENSOR, DataType.TENSOR), List.of(NumberSet.TENSOR_R, NumberSet.TENSOR_R)),
                        new Sig(List.of(DataType.TENSOR), List.of(NumberSet.TENSOR_R)),
                        U.setOf(Component.EMPTY_GEOMETRY, Component.MASS, Component.ENERGY, Component.SPACE, Component.TIME))
        );
    }
}

// ========================= Numeric Compatibility =========================
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

// ========================= Structure Adapters =========================
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

// ========================= Binding Search (all combinations) =========================
final class Binder {

    record Coercion(NumberSet from, NumberSet to, double cost, String note) {}
    static Coercion coerce(NumberSet have, NumberSet want){
        if (have==want) return new Coercion(have,want,0.0,"exact");
        if (!NumCompat.accepts(have,want)) return null;
        double cost = switch (want){
            case Z -> 0.05; case Q -> 0.08; case R -> 0.12; case C -> 0.18;
            case VECTOR_Rn, TENSOR_R -> 0.22;
            default -> 0.20;
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

    private static void backtrack(int i, List<List<Choice>> buckets, List<Choice> acc,
                                  EquationFamily fam, List<Binding> out){
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
                        Set<Triple> covers, double overlapPenalty,
                        double score, List<String> notes){
            this.fam=fam; this.picks=picks; this.structCost=structCost; this.numCost=numCost;
            this.coverageGain=coverageGain; this.covers=covers; this.overlapPenalty=overlapPenalty;
            this.score=score; this.notes = notes;
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
                else line.append("[adapt "+p.v.dtype()+"→"+wantT+"; cost="+U.fmt(p.structCost)+"]");
                if (p.numCost==0.0) line.append(" [numbers: "+wantN+" ✓]");
                else line.append(" [coerce "+p.v.nset()+"→"+wantN+"; cost="+U.fmt(p.numCost)+"]");
                notes.add(line.toString());
            }

            double score = covGain - (0.7*sCost + 0.5*nCost);
            return new Binding(fam, List.copyOf(picks), sCost, nCost, covGain, cover, 0.0, score, notes);
        }
    }
}

// ========================= Planner with Explanations =========================
final class Planner {

    record Chosen(Binder.Binding binding, String explanation) {}

    record Plan(List<Chosen> chosen, Set<Binder.Triple> covered, Set<Binder.Triple> gaps, double totalScore) {}

    static Planner.Plan choose(List<Binder.Binding> candidates, List<Variable> vars, List<EquationFamily> catalog){

        var universe = new HashSet<Binder.Triple>();
        for (var v : vars) for (var c : v.comps()) universe.add(new Binder.Triple(v.dtype(), v.nset(), c));

        var covered = new HashSet<Binder.Triple>();
        var picked  = new ArrayList<Chosen>();
        double total = 0.0;

        while (true){
            Binder.Binding best = null;
            double bestMarginal = 0.0;
            Set<Binder.Triple> bestNew = null;
            long bestOverlap = 0;

            for (var b : candidates){
                var newSet = new HashSet<Binder.Triple>(b.covers);
                newSet.removeAll(covered);
                long newCover = newSet.size();
                if (newCover==0) continue;

                long overlap = b.covers.size() - newCover;
                double penalty = 0.15 * overlap;
                double marginal = (newCover) - (0.7*b.structCost + 0.5*b.numCost + penalty);

                if (marginal > bestMarginal){
                    bestMarginal = marginal;
                    best = b; bestNew = newSet; bestOverlap = overlap;
                }
            }
            if (best==null) break;

            var sb = new StringBuilder();
            sb.append("Picked: ").append(best.fam.family()).append("\n");
            sb.append("  • New coverage gained: ").append(bestNew.size()).append("\n");
            sb.append("  • Overlap with existing: ").append(bestOverlap).append("\n");
            sb.append("  • Costs: structure=").append(U.fmt(best.structCost))
                    .append(", numeric=").append(U.fmt(best.numCost))
                    .append(", marginal score=").append(U.fmt(bestMarginal)).append("\n");
            sb.append("  • Inputs:\n");
            for (var line : best.notes) sb.append("     - ").append(line).append("\n");
            sb.append("  • Adds coverage:\n");
            for (var t : bestNew)
                sb.append("     - ").append(t.dt()).append(" × ").append(t.ns()).append(" × ").append(t.comp()).append("\n");

            picked.add(new Chosen(best, sb.toString()));
            covered.addAll(best.covers);
            total += bestMarginal;

            candidates.removeIf(b -> b.covers.stream().allMatch(covered::contains));
        }

        var gaps = new HashSet<>(universe);
        gaps.removeAll(covered);
        return new Plan(picked, covered, gaps, total);
    }
}

// ========================= Gap Advisor =========================
final class GapAdvisor {
    record Suggestion(Binder.Triple gap, String hint, double cost) {}

    static List<Suggestion> suggest(Set<Binder.Triple> gaps, List<Variable> vars){
        var out = new ArrayList<Suggestion>();
        for (var g : gaps){
            Variable bestV = null; Adapter bestA = null; Binder.Coercion bestC = null;
            double bestCost = Double.POSITIVE_INFINITY;

            for (var v : vars){
                if (!v.comps().contains(g.comp())) continue;

                double sCost = Double.POSITIVE_INFINITY; Adapter pickedA = null;
                if (v.dtype()==g.dt()){ sCost = 0.0; }
                else {
                    for (var a : AdapterRegistry.admissible(v.dtype(), g.dt())){
                        if (a.cost() < sCost){ sCost = a.cost(); pickedA = a; }
                    }
                }
                var co = Binder.coerce(v.nset(), g.ns());
                if (co==null) continue;

                double total = sCost + co.cost();
                if (total < bestCost){
                    bestCost = total; bestV = v; bestA = pickedA; bestC = co;
                }
            }

            if (bestV!=null){
                String hint = (bestA==null)
                        ? "Coerce "+bestV.name()+": "+bestC.from()+"→"+bestC.to()
                        : "Adapt "+bestV.name()+": "+bestA.from()+"→"+bestA.to()+" ("+bestA.note()+"), then "+bestC.from()+"→"+bestC.to();
                out.add(new Suggestion(g, hint, bestCost));
            } else {
                out.add(new Suggestion(g, "No single-step fix; add variable of type "+g.dt()+"×"+g.ns()+" with "+g.comp(), 1.0));
            }
        }
        out.sort(Comparator.comparingDouble(Suggestion::cost));
        return out;
    }
}

// ========================= Discovery Planner + Printer =========================
final class DiscoveryPlanner {

    record Report(List<Binder.Binding> allBindings, Planner.Plan plan, List<GapAdvisor.Suggestion> suggestions) {}

    static Report run(List<Variable> vars, List<EquationFamily> catalog){
        var all = new ArrayList<Binder.Binding>();
        for (var fam : catalog){
            if (!haveComponents(vars, fam.reqComps())) continue;
            var bs = Binder.explore(fam, vars);
            all.addAll(bs);
        }
        var plan = Planner.choose(new ArrayList<>(all), vars, catalog);
        var tips = GapAdvisor.suggest(plan.gaps(), vars);
        return new Report(all, plan, tips);
    }

    private static boolean haveComponents(List<Variable> vars, Set<Component> req){
        var have = new HashSet<Component>();
        vars.forEach(v -> have.addAll(v.comps()));
        return have.containsAll(req);
    }
}

final class PlanPrinter {
    static void print(DiscoveryPlanner.Report report){
        System.out.println("============== PLAN ==============");
        System.out.println("Total score: " + U.fmt(report.plan().totalScore()));
        System.out.println();

        int i=1;
        for (var ch : report.plan().chosen()){
            System.out.println("[" + i + "] " + ch.binding().fam.family());
            System.out.print(ch.explanation());
            System.out.println();
            i++;
        }

        System.out.println("—— Remaining gaps ("+report.plan().gaps().size()+") ——");
        report.plan().gaps().stream()
                .sorted((a,b) -> a.dt().name().compareTo(b.dt().name()))
                .forEach(g -> System.out.println("  ✗ " + g.dt()+" × "+g.ns()+" × "+g.comp()));

        System.out.println();
        System.out.println("—— Cheapest suggestions ——");
        for (var s : report.suggestions())
            System.out.println("  ? " + s.gap().dt()+" × "+s.gap().ns()+" × "+s.gap().comp()
                    + "  →  " + s.hint() + "  (≈" + U.fmt(s.cost()) + ")");
    }
}

// ========================= Symbolic Discovery (typed + dimensional) =========================
sealed interface Expr permits VarE, ConstE, Unary, Binary, OpCall {
    DataType dtype(); NumberSet nset(); Dim dim(); int size();
}
record VarE(Variable v) implements Expr {
    public DataType dtype(){return v.dtype();}
    public NumberSet nset(){return v.nset();}
    public Dim dim(){return v.dim();}
    public int size(){return 1;}
    public String toString(){ return v.name(); }
}
record ConstE(String name, NumberSet nset, Dim dim) implements Expr {
    public DataType dtype(){ return DataType.SET; }
    public int size(){ return 1; }
    public String toString(){ return name; }
}
enum UOp { NEG, SQRT, EXP, LOG, SIN, COS }
enum BOp { ADD, SUB, MUL, DIV, POW }
record Unary(UOp op, Expr x) implements Expr {
    public DataType dtype(){ return x.dtype(); }
    public NumberSet nset(){ return x.nset(); }
    public Dim dim(){
        return switch(op){
            case SQRT -> new Dim(x.dim().L()/2, x.dim().M()/2, x.dim().T()/2, x.dim().Q()/2);
            case NEG  -> x.dim();
            case EXP, SIN, COS, LOG -> Dim.ONE();
        };
    }
    public int size(){ return 1 + x.size(); }
    public String toString(){ return op+"("+x+")"; }
}
record Binary(BOp op, Expr a, Expr b) implements Expr {
    public DataType dtype(){ return a.dtype(); }
    public NumberSet nset(){ return a.nset(); }
    public Dim dim(){
        return switch(op){
            case ADD, SUB -> a.dim();
            case MUL      -> a.dim().add(b.dim());
            case DIV      -> a.dim().sub(b.dim());
            case POW      -> { // b must be dimensionless const like 2 or 1/2
                if (b instanceof ConstE cb && (cb.name().matches("-?\\d+") || cb.name().equals("1/2")))
                    yield a.dim().mul(cb.name().equals("1/2")? 1 : Integer.parseInt(cb.name()));
                yield a.dim();
            }
        };
    }
    public int size(){ return 1 + a.size() + b.size(); }
    public String toString(){ return "("+a+" "+op+" "+b+")"; }
}
enum HighOp { GRAD, DIVG, CURL, LAPLACIAN, D_DT }
record OpCall(HighOp op, Expr x) implements Expr {
    public DataType dtype(){
        return switch(op){
            case GRAD      -> DataType.MAP;
            case DIVG      -> DataType.SET;
            case CURL      -> DataType.MAP;
            case LAPLACIAN -> x.dtype();
            case D_DT      -> x.dtype();
        };
    }
    public NumberSet nset(){ return x.nset(); }
    public Dim dim(){
        return switch(op){
            case GRAD      -> x.dim().sub(Dim.unitL());
            case DIVG      -> x.dim().sub(Dim.unitL());
            case CURL      -> x.dim().sub(Dim.unitL());
            case LAPLACIAN -> x.dim().sub(Dim.unitL().mul(2));
            case D_DT      -> x.dim().sub(Dim.unitT());
        };
    }
    public int size(){ return 1 + x.size(); }
    public String toString(){ return op+"["+x+"]"; }
}

final class TypeRules {
    static boolean unaryOK(UOp op, Expr x){
        if ((op==UOp.EXP || op==UOp.SIN || op==UOp.COS || op==UOp.LOG) && !x.dim().same(Dim.ONE())) return false;
        return true;
    }
    static boolean binaryOK(BOp op, Expr a, Expr b){
        if ((op==BOp.ADD || op==BOp.SUB) && !a.dim().same(b.dim())) return false;
        if (op==BOp.POW && !(b instanceof ConstE)) return false;
        return true;
    }
    static boolean highOK(HighOp op, Expr x){
        return switch (op){
            case GRAD, CURL, DIVG, LAPLACIAN -> (x.dtype()==DataType.MAP || x.dtype()==DataType.GRAPH || x.dtype()==DataType.MATRIX);
            case D_DT -> true;
        };
    }
}

record Template(Expr left, Expr right, List<ConstE> params, Set<String> missingVars, double complexity, String rationale){}

// ========================= Dimensional Solver =========================
final class DimSolver {
    record Term(String symbol, Dim dim) {}

    // Solve Σ a_i * dim_i = 0 in a crude way (suggest relative exponents)
    static Map<String, Double> solve(List<Term> terms){
        Map<String, Double> sol = new LinkedHashMap<>();
        if (terms.isEmpty()) return sol;
        // Simple heuristic: set first exponent = 1.0, balance each dimension via ratios vs first
        Dim d0 = terms.get(0).dim();
        sol.put(terms.get(0).symbol(), 1.0);
        for (int i=1;i<terms.size();i++){
            Dim di = terms.get(i).dim();
            double aL = balance(di.L(), d0.L());
            double aM = balance(di.M(), d0.M());
            double aT = balance(di.T(), d0.T());
            double aQ = balance(di.Q(), d0.Q());
            double avg = avgNonNan(aL,aM,aT,aQ);
            sol.put(terms.get(i).symbol(), avg);
        }
        return sol;
    }
    private static double balance(int a, int b){ return (b==0)? Double.NaN : -(double)b/(double)a; }
    private static double avgNonNan(double... xs){
        double s=0; int n=0; for(double x:xs){ if(!Double.isNaN(x) && Double.isFinite(x)){ s+=x; n++; } }
        return n==0? 0.0 : s/n;
    }
}

// ========================= Symbolic Enumerator =========================
final class SymbolicEnumerator {
    record Library(Set<UOp> uops, Set<BOp> bops, Set<HighOp> hops){}
    static Library defaultLib(){
        return new Library(
                U.setOf(UOp.NEG, UOp.SQRT, UOp.EXP, UOp.LOG, UOp.SIN, UOp.COS),
                U.setOf(BOp.ADD,BOp.SUB,BOp.MUL,BOp.DIV,BOp.POW),
                U.setOf(HighOp.GRAD, HighOp.DIVG, HighOp.CURL, HighOp.LAPLACIAN, HighOp.D_DT)
        );
    }

    static List<Template> enumerate(List<Variable> vars, List<PhysicalConstant> constants,
                                    Library lib, int maxSize, Set<Component> context){

        var pool = new ArrayList<Expr>();
        for (var v : vars) pool.add(new VarE(v));
        // inject constants allowed by context
        for (var pc : constants) if (!Collections.disjoint(pc.comps(), context))
            pool.add(new ConstE(pc.id().name(), pc.nset(), pc.dim()));

        // add simple scalar exponents for POW
        pool.add(new ConstE("2", NumberSet.R, Dim.ONE()));
        pool.add(new ConstE("1/2", NumberSet.R, Dim.ONE()));

        var exprs = new HashSet<Expr>(pool);
        for (int s=2; s<=maxSize; s++){
            var next = new HashSet<Expr>();
            for (var e : exprs){
                for (var u : lib.uops){ var cand = new Unary(u, e); if (TypeRules.unaryOK(u, e)) next.add(cand); }
                for (var h : lib.hops){ if (TypeRules.highOK(h, e)) next.add(new OpCall(h, e)); }
                for (var f : exprs){
                    for (var bop : lib.bops){
                        var cand = new Binary(bop, e, f);
                        if (TypeRules.binaryOK(bop, e, f)) next.add(cand);
                    }
                }
            }
            exprs.addAll(next);
        }

        var templates = new ArrayList<Template>();
        var list = new ArrayList<>(exprs);
        for (var a : list){
            for (var b : list){
                if (!a.dim().same(b.dim())) continue;
                var rationale = rationaleFor(a,b,context);
                if (rationale==null) continue;

                var params = new ArrayList<ConstE>(); // can extract specific constants if you track usage
                var missing = new HashSet<String>();
                if (context.contains(Component.TIME) && !usesOp(a, HighOp.D_DT) && !usesOp(b, HighOp.D_DT)){
                    missing.add("∂/∂t term");
                }
                double complexity = a.size() + b.size();

                // run dimension suggestion (even if matched, helpful for exponents)
                var terms = List.of(new DimSolver.Term(a.toString(), a.dim()),
                        new DimSolver.Term(b.toString(), b.dim().mul(-1)));
                var dimFix = DimSolver.solve(terms);
                String why = rationale + " | DimFix: " + dimFix;

                templates.add(new Template(a,b,params,missing,complexity,why));
            }
        }
        return templates;
    }

    private static boolean usesOp(Expr e, HighOp op){
        if (e instanceof OpCall oc) return oc.op()==op || usesOp(oc.x(), op);
        if (e instanceof Unary u)   return usesOp(u.x(), op);
        if (e instanceof Binary b)  return usesOp(b.a(), op) || usesOp(b.b(), op);
        return false;
    }

    private static String rationaleFor(Expr a, Expr b, Set<Component> ctx){
        var s = a.toString()+" = "+b.toString();
        if (s.contains("CURL") && ctx.contains(Component.RF)) return "Vector-rotation relation (EM-like)";
        if (s.contains("DIVG") && ctx.contains(Component.RF)) return "Source relation (Gauss-like)";
        if (s.contains("LAPLACIAN") && ctx.contains(Component.TIME)) return "Parabolic/Hyperbolic PDE structure";
        if (s.contains("D_DT") && ctx.contains(Component.TIME)) return "Time-evolution form";
        if (ctx.contains(Component.SPACE) || ctx.contains(Component.TIME)) return "Structure-compatible equality";
        return null;
    }
}

// ========================= Scoring & Printing for Symbolic =========================
record Candidate(Template tpl, double score, List<String> proofSteps, Set<String> newConstants, Set<String> missingVars){}

final class Scorer {
    static Candidate assess(Template t, Set<Component> ctx){
        double length = t.complexity();
        double miss   = t.missingVars().size();
        double contextReward = 0.0;
        if (t.rationale().contains("EM")) contextReward += 1.0;
        if (t.rationale().contains("Gauss")) contextReward += 0.7;
        if (t.rationale().contains("Time")) contextReward += 0.5;

        var newConsts = new HashSet<String>(); // extend if you parse constants out of templates
        double score = contextReward - 0.1*length - 0.5*miss;

        var proof = new ArrayList<String>();
        proof.add("Type/units matched: " + t.left().dim());
        proof.add("Operators permitted by structure.");
        if (miss>0) proof.add("Missing terms: " + t.missingVars());
        proof.add("Rationale: " + t.rationale());
        return new Candidate(t, score, proof, newConsts, t.missingVars());
    }
}

final class SymbolicDiscovery {
    record DiscoveryResult(List<Candidate> ranked, List<String> globalNotes) {}

    static DiscoveryResult discover(List<Variable> vars, Set<Component> context, int maxExprSize){
        var constants = ConstantSet.all();
        var templates = SymbolicEnumerator.enumerate(vars, constants, SymbolicEnumerator.defaultLib(), maxExprSize, context);

        var cands = new ArrayList<Candidate>();
        for (var t : templates){
            var c = Scorer.assess(t, context);
            if (c.score()>0) cands.add(c);
        }
        cands.sort((a,b)-> Double.compare(b.score(), a.score()));

        var notes = new ArrayList<String>();
        long withCurl = cands.stream().limit(10).filter(c -> c.tpl().left().toString().contains("CURL") || c.tpl().right().toString().contains("CURL")).count();
        if (withCurl>0 && context.contains(Component.RF)) notes.add("Detected curl-based relations consistent with RF components.");
        var mv = cands.stream().flatMap(c -> c.missingVars().stream()).distinct().toList();
        if (!mv.isEmpty()) notes.add("Common missing terms: " + mv);

        return new DiscoveryResult(cands, notes);
    }

    static void printTop(DiscoveryResult dr, int N){
        System.out.println("=== Discovered candidate equations ===");
        for (int i=0; i<Math.min(N, dr.ranked().size()); i++){
            var c = dr.ranked().get(i);
            System.out.println("["+(i+1)+"]  " + c.tpl().left() + "  =  " + c.tpl().right());
            System.out.println("     score: " + U.fmt(c.score()));
            for (var step : c.proofSteps()) System.out.println("     • " + step);
            if (!c.newConstants().isEmpty()) System.out.println("     • Introduce constants: " + c.newConstants());
            if (!c.missingVars().isEmpty())  System.out.println("     • Missing variables/terms: " + c.missingVars());
        }
        System.out.println();
        if (!dr.globalNotes().isEmpty()){
            System.out.println("Notes:");
            dr.globalNotes().forEach(n -> System.out.println("  - " + n));
        }
    }
}

// ========================= Scenarios =========================
final class ScenarioUtil {
    static Variable scalar(String name, NumberSet ns, Set<Component> comps, Dim dim){
        return new Variable(name, DataType.SET, ns, comps, dim);
    }
    static Variable list(String name, NumberSet ns, Set<Component> comps, Dim dim){
        return new Variable(name, DataType.LIST, ns, comps, dim);
    }
    static Variable map(String name, NumberSet ns, Set<Component> comps, Dim dim){
        return new Variable(name, DataType.MAP, ns, comps, dim);
    }
    static Variable matrix(String name, NumberSet ns, Set<Component> comps, Dim dim){
        return new Variable(name, DataType.MATRIX, ns, comps, dim);
    }
    static Variable graph(String name, NumberSet ns, Set<Component> comps, Dim dim){
        return new Variable(name, DataType.GRAPH, ns, comps, dim);
    }
    static Variable tensor(String name, NumberSet ns, Set<Component> comps, Dim dim){
        return new Variable(name, DataType.TENSOR, ns, comps, dim);
    }
}

final class AtomicScenario {
    static List<Variable> vars(){
        return List.of(
                ScenarioUtil.map("E", NumberSet.R,
                        U.setOf(Component.RF, Component.SPACE, Component.TIME),
                        Dim.unitM().add(Dim.unitL()).sub(Dim.unitT().mul(2))),  // illustrative

                ScenarioUtil.map("B", NumberSet.R,
                        U.setOf(Component.RF, Component.SPACE, Component.TIME),
                        Dim.unitM().sub(Dim.unitT().mul(2))),

                ScenarioUtil.map("rho", NumberSet.R,
                        U.setOf(Component.ENERGY, Component.SPACE),
                        Dim.unitM().sub(Dim.unitL().mul(3))),

                ScenarioUtil.map("J", NumberSet.R,
                        U.setOf(Component.ENERGY, Component.SPACE, Component.TIME),
                        Dim.unitM().sub(Dim.unitL().mul(2)).sub(Dim.unitT())),

                ScenarioUtil.list("psi", NumberSet.R,
                        U.setOf(Component.TIME, Component.ENERGY),
                        Dim.ONE()),

                ScenarioUtil.matrix("heat_field", NumberSet.R,
                        U.setOf(Component.ENERGY, Component.SPACE, Component.TIME),
                        Dim.ENERGY())
        );
    }
    static Set<Component> context(){
        return U.setOf(Component.SPACE, Component.TIME, Component.RF, Component.ENERGY);
    }
}

// (Other scenarios omitted here for brevity; you can paste Quark/Material/Chemistry from earlier if desired)

// ========================= BLACK-BOX O→I PREDICTOR =========================
/*
   Idea:
   - Take inputs (variable name → numerical placeholder) and the chosen plan + top symbolic candidates.
   - Predict which outputs change (qualitatively) and which NEW inputs should be created/updated.
   - Emit a simple ASCII "black box" visual of the flow.
   - Return a "BehaviorUpdate" that you can use to mutate your system state (e.g., add variables, adapters).
*/
final class OIPredictor {

    record IO(Map<String, Double> inputs, Map<String, Double> outputs) {}

    record BehaviorUpdate(
            Set<String> suggestNewVariables,          // names/types to add
            Set<String> suggestConstants,             // constants needed (from contexts)
            Set<String> suggestAdapters,              // e.g., LIST→MAP
            Set<String> triggeredFamilies,            // families likely to activate
            List<String> narrative                    // readable explanation
    ) {}

    static BehaviorUpdate predict(IO io, DiscoveryPlanner.Report plan, SymbolicDiscovery.DiscoveryResult sym){
        var newVars   = new LinkedHashSet<String>();
        var newConsts = new LinkedHashSet<String>();
        var adapters  = new LinkedHashSet<String>();
        var fams      = new LinkedHashSet<String>();
        var story     = new ArrayList<String>();

        // 1) From plan: which families are chosen → outputs likely respond
        for (var ch : plan.plan().chosen()){
            fams.add(ch.binding().fam.family());
            story.add("Family engaged: "+ch.binding().fam.family());
            // surface adapters/coercions used
            for (var n : ch.binding().notes){
                if (n.contains("adapt")) adapters.add(n.trim());
            }
        }

        // 2) From symbolic: top 5 candidates suggest constants/terms
        int topN = Math.min(5, sym.ranked().size());
        for (int i=0;i<topN;i++){
            var c = sym.ranked().get(i);
            if (!c.missingVars().isEmpty()) newVars.addAll(c.missingVars());
            if (!c.newConstants().isEmpty()) newConsts.addAll(c.newConstants());
            story.add("Equation candidate: " + c.tpl().left() + " = " + c.tpl().right() + " (score " + U.fmt(c.score()) + ")");
        }

        // 3) Inputs drive outputs qualitatively
        for (var e : io.inputs().entrySet()){
            story.add("Input '"+e.getKey()+"' perturbs downstream fields via engaged families.");
        }

        return new BehaviorUpdate(newVars, newConsts, adapters, fams, story);
    }

    // Console "black box" visual
    static void printBlackBox(IO io, BehaviorUpdate bu){
        U.line();
        System.out.println("BLACK BOX v1 — Output/Input Equation Predictor");
        U.line();
        System.out.println("   Inputs:");
        io.inputs().forEach((k,v) -> System.out.println("     → " + k + "  (" + U.fmt(v) + ")"));
        U.line();
        System.out.println("   ┌───────────────────────────────┐");
        System.out.println("   │           BLACK BOX           │");
        System.out.println("   │  (discovered laws + symbolic) │");
        System.out.println("   └───────────────────────────────┘");
        U.line();
        System.out.println("   Predicted active families:");
        bu.triggeredFamilies().forEach(f -> System.out.println("     • " + f));
        System.out.println("   Suggested adapters:");
        if (bu.suggestAdapters().isEmpty()) System.out.println("     • (none)");
        else bu.suggestAdapters().forEach(a -> System.out.println("     • " + a));
        System.out.println("   Suggested new variables/terms:");
        if (bu.suggestNewVariables().isEmpty()) System.out.println("     • (none)");
        else bu.suggestNewVariables().forEach(a -> System.out.println("     • " + a));
        System.out.println("   Suggested constants:");
        if (bu.suggestConstants().isEmpty()) System.out.println("     • (none)");
        else bu.suggestConstants().forEach(a -> System.out.println("     • " + a));

        U.line();
        System.out.println("   Narrative:");
        bu.narrative().forEach(s -> System.out.println("     - " + s));
        U.line();

        // Outputs are placeholder here; plug real evaluation when you attach data
        System.out.println("   Outputs (qualitative):");
        System.out.println("     → fields updated; equations instantiated; gaps reduced.");
        U.line();
    }
}

// ========================= Demo: wire everything + black box =========================
public class ExistenceEquationDiscovery {

    static List<Variable> varsAtomic(){
        return AtomicScenario.vars();
    }

    public static void main(String[] args){
        // —— 1) Seed variables for the Atomic scenario
        var vars = varsAtomic();
        var catalog = PhysicsCatalog.families();

        // —— 2) Type-driven discovery & plan
        var report = DiscoveryPlanner.run(vars, catalog);
        PlanPrinter.print(report);

        // —— 3) Symbolic discovery (EM-flavored context)
        var ctx = AtomicScenario.context();
        var sym = SymbolicDiscovery.discover(vars, ctx, 4);
        System.out.println();
        SymbolicDiscovery.printTop(sym, 6);

        // —— 4) Output→Input predictor: feed “inputs” and see the box reason about behavior
        var inputs = new LinkedHashMap<String, Double>();
        inputs.put("E (external drive)", 1.0);
        inputs.put("rho (charge injection)", 0.2);
        var io = new OIPredictor.IO(inputs, Map.of()); // outputs qualitative for now

        var bu = OIPredictor.predict(io, report, sym);
        OIPredictor.printBlackBox(io, bu);

        // —— 5) (Optional) use BehaviorUpdate to mutate your system:
        // e.g., add missing ∂/∂t term as a variable, or accept an adapter LIST→MAP for psi
        // Here we just print the suggested actions.
        System.out.println("Next actions:");
        if (!bu.suggestNewVariables().isEmpty())
            System.out.println("  - Add variables/terms: " + bu.suggestNewVariables());
        if (!bu.suggestAdapters().isEmpty())
            System.out.println("  - Apply adapters: " + bu.suggestAdapters());
        if (!bu.suggestConstants().isEmpty())
            System.out.println("  - Expose constants: " + bu.suggestConstants());
    }
}
