//package org.project.equationgenerator;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
///* ===========================================================
//   EQUATION GENERATOR — DIMENSIONAL + GRAMMAR + EMPIRICAL CHECK
//   Domains: GR, QED, QM, QCD (synthetic-yet-plausible datasets)
//   =========================================================== */
//
///* ============== Utils ============== */
//final class Util {
//    static <T> Set<T> setOf(T... xs){ return new HashSet<>(List.of(xs)); }
//    static String fmt(double x){ return String.format("%.6g", x); }
//    static void line(){ System.out.println("--------------------------------------------------"); }
//    static double clamp(double v,double a,double b){ return Math.max(a, Math.min(b, v)); }
//}
//
///* ============== Core Taxonomy ============== */
//enum DataType { SET, LIST, MAP, MATRIX, GRAPH, TREE, TENSOR }
//enum NumberSet { N, Z, Q, R, C, GFp, BOOL, INTERVAL, VECTOR_Rn, TENSOR_R }
//enum Component { SPACE, MASS, TIME, ENERGY, RF, EMPTY_GEOMETRY }
//
///* ============== Dimensions (SI base L, M, T, Q) ============== */
//record Dim(int L, int M, int T, int Q){
//    static Dim unitL(){return new Dim(1,0,0,0);}
//    static Dim unitM(){return new Dim(0,1,0,0);}
//    static Dim unitT(){return new Dim(0,0,1,0);}
//    static Dim unitQ(){return new Dim(0,0,0,1);}
//    static Dim ONE(){return new Dim(0,0,0,0);}
//    static Dim ENERGY(){return new Dim(2,1,-2,0);} // ML^2/T^2
//    Dim add(Dim o){ return new Dim(L+o.L, M+o.M, T+o.T, Q+o.Q); }
//    Dim sub(Dim o){ return new Dim(L-o.L, M-o.M, T-o.T, Q-o.Q); }
//    Dim mul(int k){ return new Dim(k*L, k*M, k*T, k*Q); }
//    boolean same(Dim o){ return L==o.L && M==o.M && T==o.T && Q==o.Q; }
//    int[] v(){ return new int[]{L,M,T,Q}; }
//    public String toString(){ return "L^"+L+" M^"+M+" T^"+T+" Q^"+Q; }
//}
//
///* ============== Variables ============== */
//record Variable(String name, DataType dtype, NumberSet nset, Set<Component> comps, Dim dim) {}
//
///* ============== Constants (first-class, with numeric values) ============== */
//enum ConstantID { PI, E, SQRT2, C_LIGHT, HBAR, G_NEWTON, K_BOLTZ, EPS0, MU0 }
//
//record PhysicalConstant(ConstantID id, NumberSet nset, Dim dim, Set<Component> comps, String note, double value) {}
//
//final class ConstantSet {
//    private static final List<PhysicalConstant> BASE = List.of(
//            new PhysicalConstant(ConstantID.PI,     NumberSet.R, Dim.ONE(), Util.setOf(Component.EMPTY_GEOMETRY), "circle ratio", Math.PI),
//            new PhysicalConstant(ConstantID.E,      NumberSet.R, Dim.ONE(), Util.setOf(Component.EMPTY_GEOMETRY), "natural base", Math.E),
//            new PhysicalConstant(ConstantID.SQRT2,  NumberSet.R, Dim.ONE(), Util.setOf(Component.EMPTY_GEOMETRY), "diag of unit square", Math.sqrt(2)),
//            new PhysicalConstant(ConstantID.C_LIGHT,NumberSet.R, Dim.unitL().sub(Dim.unitT()), Util.setOf(Component.SPACE, Component.TIME, Component.RF), "speed of light", 299_792_458.0),
//            new PhysicalConstant(ConstantID.HBAR,   NumberSet.R, Dim.unitM().add(Dim.unitL().mul(2)).sub(Dim.unitT()), Util.setOf(Component.ENERGY, Component.TIME), "reduced Planck", 1.054_571_817e-34),
//            new PhysicalConstant(ConstantID.G_NEWTON,NumberSet.R, Dim.unitL().mul(3).sub(Dim.unitM()).sub(Dim.unitT().mul(2)), Util.setOf(Component.MASS, Component.SPACE, Component.TIME), "Newton G", 6.67430e-11),
//            new PhysicalConstant(ConstantID.K_BOLTZ,NumberSet.R, Dim.ENERGY(), Util.setOf(Component.ENERGY), "Boltzmann (K suppressed)", 1.380_649e-23),
//            new PhysicalConstant(ConstantID.EPS0,   NumberSet.R, Dim.unitM().sub(Dim.unitL().mul(3)).sub(Dim.unitT().mul(2)).add(Dim.unitQ().mul(2)), Util.setOf(Component.RF, Component.SPACE, Component.TIME), "vacuum permittivity", 8.854_187_8128e-12),
//            new PhysicalConstant(ConstantID.MU0,    NumberSet.R, Dim.unitM().add(Dim.unitL()).sub(Dim.unitQ().mul(2)), Util.setOf(Component.RF, Component.SPACE, Component.TIME), "vacuum permeability", 1.256_637_06212e-6)
//    );
//    private static final List<PhysicalConstant> DYNAMIC = new ArrayList<>();
//
//    static List<PhysicalConstant> all(){ var out=new ArrayList<PhysicalConstant>(BASE); out.addAll(DYNAMIC); return out; }
//
//    static void promote(String symbol, Dim dim, Set<Component> comps, String note, double numeric){
//        DYNAMIC.add(new PhysicalConstant(null, NumberSet.R, dim, comps, note+" ["+symbol+"]", numeric));
//    }
//
//    static OptionalDouble numeric(String name){
//        for (var c: all()){
//            var id = (c.id()==null? "KAPPA" : c.id().name());
//            if (id.equals(name)) return OptionalDouble.of(c.value());
//        }
//        return OptionalDouble.empty();
//    }
//}
//
///* ============== Grammars I–X (weights just nudge scoring) ============== */
//enum GrammarID { I, II, III, IV, V, VI, VII, VIII, IX, X }
//record GrammarSpec(GrammarID id, String name, String symbol, String domain, String role,
//                   String invariants, String transition, String equilibrium){}
//record GrammarContext(Map<GrammarID,Double> weight){
//    double w(GrammarID g){ return weight.getOrDefault(g, 1.0); }
//    static GrammarContext defaultFocus(){
//        return new GrammarContext(Map.of(
//                GrammarID.I,1.0, GrammarID.II,1.0, GrammarID.VI,1.2,
//                GrammarID.VIII,1.0, GrammarID.IX,1.1, GrammarID.X,1.0, GrammarID.III,1.1
//        ));
//    }
//}
//
///* ============== Law “families” (kept minimal; templates will carry weight) ============== */
//record Sig(List<DataType> dtypes, List<NumberSet> nsets) {}
//record EquationFamily(String family, Sig inputSig, Sig outputSig, Set<Component> reqComps) {}
//
///* ============== Numeric compatibility & adapters (for structure scoring) ============== */
//final class NumCompat {
//    static boolean accepts(NumberSet have, NumberSet want){
//        if (have==want) return true;
//        return switch (want){
//            case Z -> have==NumberSet.N || have==NumberSet.BOOL;
//            case Q -> have==NumberSet.N || have==NumberSet.Z;
//            case R -> have==NumberSet.N || have==NumberSet.Z || have==NumberSet.Q || have==NumberSet.INTERVAL;
//            case C -> have==NumberSet.N || have==NumberSet.Z || have==NumberSet.Q || have==NumberSet.R;
//            case VECTOR_Rn, TENSOR_R -> have==NumberSet.R;
//            default -> false;
//        };
//    }
//}
//record Adapter(DataType from, DataType to, double cost, String note) {}
//final class AdapterRegistry {
//    static List<Adapter> adapters(){
//        return List.of(
//                new Adapter(DataType.LIST,   DataType.MAP,    0.25, "Index keys from list (k→v)"),
//                new Adapter(DataType.LIST,   DataType.MATRIX, 0.35, "Embed list as column vector"),
//                new Adapter(DataType.MAP,    DataType.MATRIX, 0.30, "Linearize map to operator"),
//                new Adapter(DataType.MATRIX, DataType.TENSOR, 0.30, "Promote matrix to rank-2 tensor"),
//                new Adapter(DataType.SET,    DataType.LIST,   0.20, "Canonical ordering"),
//                new Adapter(DataType.MAP,    DataType.LIST,   0.25, "Iterate entries"),
//                new Adapter(DataType.LIST,   DataType.SET,    0.10, "Deduplicate to set")
//        );
//    }
//    static List<Adapter> admissible(DataType have, DataType want){
//        var out = new ArrayList<Adapter>();
//        for (var a : adapters()) if (a.from()==have && a.to()==want) out.add(a);
//        return out;
//    }
//}
//
///* ============== Binder (score which family matches variable pool) ============== */
//final class Binder {
//    record Coercion(NumberSet from, NumberSet to, double cost, String note) {}
//    static Coercion coerce(NumberSet have, NumberSet want){
//        if (have==want) return new Coercion(have,want,0.0,"exact");
//        if (!NumCompat.accepts(have,want)) return null;
//        double cost = switch (want){
//            case Z -> 0.05; case Q -> 0.08; case R -> 0.12; case C -> 0.18;
//            case VECTOR_Rn, TENSOR_R -> 0.22; default -> 0.20;
//        };
//        return new Coercion(have, want, cost, "numeric upcast "+have+"→"+want);
//    }
//    record Choice(Variable v, double structCost, double numCost, DataType asType, NumberSet asNum, String note) {}
//    static List<List<Choice>> candidateChoices(List<Variable> vars, DataType wantT, NumberSet wantN){
//        var choices = new ArrayList<Choice>();
//        for (var v : vars){
//            if (v.dtype()==wantT){
//                var cc = coerce(v.nset(), wantN);
//                if (cc!=null) choices.add(new Choice(v, 0.0, cc.cost(), wantT, wantN, "structure exact; "+cc.note()));
//            }
//            for (var a : AdapterRegistry.admissible(v.dtype(), wantT)){
//                var cc = coerce(v.nset(), wantN);
//                if (cc!=null) choices.add(new Choice(v, a.cost(), cc.cost(), wantT, wantN, "adapt "+v.dtype()+"→"+wantT+"; "+cc.note()));
//            }
//        }
//        return List.of(choices);
//    }
//    record Triple(DataType dt, NumberSet ns, Component comp) {}
//    static final class Binding {
//        final EquationFamily fam;
//        final List<Choice> picks;
//        final double structCost, numCost, coverageGain, score;
//        final Set<Triple> covers;
//        final List<String> notes;
//        private Binding(EquationFamily fam, List<Choice> picks,
//                        double structCost, double numCost, double coverageGain,
//                        Set<Triple> covers, double score, List<String> notes){
//            this.fam=fam; this.picks=picks; this.structCost=structCost; this.numCost=numCost;
//            this.coverageGain=coverageGain; this.covers=covers; this.score=score; this.notes=notes;
//        }
//        static Binding from(EquationFamily fam, List<Choice> picks){
//            double sCost = picks.stream().mapToDouble(p->p.structCost).sum();
//            double nCost = picks.stream().mapToDouble(p->p.numCost).sum();
//            var cover = new HashSet<Triple>();
//            for (int i=0;i<fam.inputSig().dtypes().size();i++){
//                var dt = fam.inputSig().dtypes().get(i);
//                var ns = fam.inputSig().nsets().get(i);
//                for (var c : fam.reqComps()) cover.add(new Triple(dt, ns, c));
//            }
//            double covGain = cover.size();
//            var notes = new ArrayList<String>();
//            for (int i=0;i<picks.size();i++){
//                var p = picks.get(i);
//                var wantT = fam.inputSig().dtypes().get(i);
//                var wantN = fam.inputSig().nsets().get(i);
//                var line = new StringBuilder("input["+i+"]: "+p.v.name()+"  ");
//                if (p.structCost==0.0) line.append("[structure: "+wantT+" ✓]");
//                else line.append("[adapt "+p.v.dtype()+"→"+wantT+"; cost="+Util.fmt(p.structCost)+"]");
//                if (p.numCost==0.0) line.append(" [numbers: "+wantN+" ✓]");
//                else line.append(" [coerce "+p.v.nset()+"→"+wantN+"; cost="+Util.fmt(p.numCost)+"]");
//                notes.add(line.toString());
//            }
//            double score = covGain - (0.7*sCost + 0.5*nCost);
//            return new Binding(fam, List.copyOf(picks), sCost, nCost, covGain, cover, score, notes);
//        }
//    }
//    static List<Binding> explore(EquationFamily fam, List<Variable> vars){
//        var buckets = new ArrayList<List<Choice>>();
//        for (int i=0;i<fam.inputSig().dtypes().size();i++){
//            var wantT = fam.inputSig().dtypes().get(i);
//            var wantN = fam.inputSig().nsets().get(i);
//            var cand = candidateChoices(vars, wantT, wantN).get(0);
//            if (cand.isEmpty()) return List.of();
//            buckets.add(cand);
//        }
//        var res = new ArrayList<Binding>();
//        backtrack(0, buckets, new ArrayList<>(), fam, res);
//        return res;
//    }
//    private static void backtrack(int i, List<List<Choice>> buckets, List<Choice> acc, EquationFamily fam, List<Binding> out){
//        if (i==buckets.size()){ out.add(Binding.from(fam, acc)); return; }
//        for (var c : buckets.get(i)){
//            acc.add(c);
//            backtrack(i+1, buckets, acc, fam, out);
//            acc.remove(acc.size()-1);
//        }
//    }
//}
//
///* ============== Grammar scoring (simple nudges) ============== */
//final class GrammarScorer {
//    static double bonus(Binder.Binding b, GrammarContext ctx){
//        double bonus=0;
//        int uniq = b.covers.size();
//        if (uniq>0){ bonus += ctx.w(GrammarID.I)*0.10*uniq; }
//        bonus += ctx.w(GrammarID.II)*(-(0.30*b.structCost + 0.20*b.numCost));
//        boolean energetic = b.fam.reqComps().contains(Component.RF) || b.fam.reqComps().contains(Component.ENERGY);
//        if (energetic) bonus+=ctx.w(GrammarID.VI)*0.2;
//        if (b.fam.reqComps().contains(Component.TIME)) bonus += ctx.w(GrammarID.IX)*0.15;
//        if (b.numCost==0.0) bonus += ctx.w(GrammarID.X)*0.1;
//        return bonus;
//    }
//}
//
///* ============== Planner (choose families) ============== */
//final class Planner {
//    record Chosen(Binder.Binding binding, String explanation) {}
//    record Plan(List<Chosen> chosen, Set<Binder.Triple> covered, Set<Binder.Triple> gaps, double totalScore) {}
//
//    static Plan choose(List<Binder.Binding> candidates, List<Variable> vars, List<EquationFamily> catalog){
//        var ctx = GrammarContext.defaultFocus();
//        var universe = new HashSet<Binder.Triple>();
//        for (var v : vars) for (var c : v.comps()) universe.add(new Binder.Triple(v.dtype(), v.nset(), c));
//        var covered = new HashSet<Binder.Triple>();
//        var picked  = new ArrayList<Chosen>();
//        double total = 0.0;
//
//        while (true){
//            Binder.Binding best = null; double bestMarg = 0; Set<Binder.Triple> bestNew=null; long bestOverlap=0; double bestBonus=0;
//            for (var b : candidates){
//                var newSet = new HashSet<Binder.Triple>(b.covers); newSet.removeAll(covered);
//                long newCover = newSet.size(); if (newCover==0) continue;
//                long overlap = b.covers.size() - newCover;
//                double penalty = 0.15*overlap;
//                double baseMarg = (newCover) - (0.7*b.structCost + 0.5*b.numCost + penalty);
//                double ge = GrammarScorer.bonus(b, ctx);
//                double marg = baseMarg + ge;
//                if (marg > bestMarg){ bestMarg=marg; best=b; bestNew=newSet; bestOverlap=overlap; bestBonus=ge; }
//            }
//            if (best==null) break;
//
//            var sb = new StringBuilder();
//            sb.append("Picked: ").append(best.fam.family()).append("\n");
//            sb.append("  • New coverage gained: ").append(bestNew.size()).append("\n");
//            sb.append("  • Overlap with existing: ").append(bestOverlap).append("\n");
//            sb.append("  • Costs: structure=").append(Util.fmt(best.structCost))
//                    .append(", numeric=").append(Util.fmt(best.numCost))
//                    .append(", marginal+grammar=").append(Util.fmt(bestMarg)).append("\n");
//            for (var line : best.notes) sb.append("     - ").append(line).append("\n");
//
//            picked.add(new Chosen(best, sb.toString()));
//            covered.addAll(best.covers);
//            total += bestMarg;
//            candidates.removeIf(b -> b.covers.stream().allMatch(covered::contains));
//        }
//        var gaps = new HashSet<>(universe); gaps.removeAll(covered);
//        return new Plan(picked, covered, gaps, total);
//    }
//}
//
///* ============== Symbolic expressions ============== */
//sealed interface Expr permits VarE, ConstE, Unary, Binary {
//    DataType dtype(); NumberSet nset(); Dim dim(); int size();
//}
//record VarE(Variable v) implements Expr {
//    public DataType dtype(){return v.dtype();}
//    public NumberSet nset(){return v.nset();}
//    public Dim dim(){return v.dim();}
//    public int size(){return 1;}
//    public String toString(){return v.name();}
//}
//record ConstE(String name, NumberSet nset, Dim dim) implements Expr {
//    public DataType dtype(){return DataType.SET;}
//    public int size(){return 1;}
//    public String toString(){return name;}
//}
//enum UOp { NEG, SQRT, EXP, LOG, SIN, COS }
//enum BOp { ADD, SUB, MUL, DIV, POW }
//record Unary(UOp op, Expr x) implements Expr {
//    public DataType dtype(){return x.dtype();}
//    public NumberSet nset(){return x.nset();}
//    public Dim dim(){
//        return switch(op){
//            case SQRT -> new Dim(x.dim().L()/2, x.dim().M()/2, x.dim().T()/2, x.dim().Q()/2);
//            case NEG  -> x.dim();
//            case EXP, SIN, COS, LOG -> Dim.ONE();
//        };
//    }
//    public int size(){return 1+x.size();}
//    public String toString(){return op+"("+x+")";}
//}
//record Binary(BOp op, Expr a, Expr b) implements Expr {
//    public DataType dtype(){return a.dtype();}
//    public NumberSet nset(){return a.nset();}
//    public Dim dim(){
//        return switch(op){
//            case ADD, SUB -> a.dim();
//            case MUL      -> a.dim().add(b.dim());
//            case DIV      -> a.dim().sub(b.dim());
//            case POW      -> {
//                if (b instanceof ConstE cb && (cb.name().matches("-?\\d+") || cb.name().equals("1/2")))
//                    yield a.dim().mul(cb.name().equals("1/2")?1:Integer.parseInt(cb.name()));
//                yield a.dim();
//            }
//        };
//    }
//    public int size(){return 1+a.size()+b.size();}
//    public String toString(){return "("+a+" "+op+" "+b+")";}
//}
//
///* ============== Type rules ============== */
//final class TypeRules {
//    static boolean unaryOK(UOp op, Expr x){
//        if ((op==UOp.EXP||op==UOp.SIN||op==UOp.COS||op==UOp.LOG) && !x.dim().same(Dim.ONE())) return false;
//        return true;
//    }
//    static boolean binaryOK(BOp op, Expr a, Expr b){
//        if ((op==BOp.ADD||op==BOp.SUB) && !a.dim().same(b.dim())) return false;
//        if (op==BOp.POW && !(b instanceof ConstE)) return false;
//        return true;
//    }
//}
//
///* ============== EXACT rationals + linear solver (dim balance) ============== */
//final class Rat implements Comparable<Rat>{
//    final long n,d; Rat(long n,long d){ long g=gcd(Math.abs(n),Math.abs(d)); long s=d<0?-1:1; this.n=s*n/g; this.d=Math.abs(d)/g; }
//    static long gcd(long a,long b){ while(b!=0){ long t=a%b; a=b; b=t; } return Math.max(1,a); }
//    static Rat of(long n){ return new Rat(n,1); }
//    static Rat add(Rat a,Rat b){ return new Rat(a.n*b.d + b.n*a.d, a.d*b.d); }
//    static Rat sub(Rat a,Rat b){ return new Rat(a.n*b.d - b.n*a.d, a.d*b.d); }
//    static Rat mul(Rat a,Rat b){ return new Rat(a.n*b.n, a.d*b.d); }
//    static Rat div(Rat a,Rat b){ return new Rat(a.n*b.d, a.d*b.n); }
//    public int compareTo(Rat o){ return Long.compare(n*o.d, o.n*d); }
//    public String toString(){ return d==1? Long.toString(n) : (n+"/"+d); }
//}
//final class LinRat {
//    static Optional<Rat[]> solve(Rat[][] A, Rat[] b){
//        int m=A.length, n=A[0].length;
//        Rat[][] M=new Rat[m][n+1];
//        for(int i=0;i<m;i++){ for(int j=0;j<n;j++) M[i][j]=A[i][j]; M[i][n]=b[i]; }
//        int row=0, col=0;
//        while(row<m && col<n){
//            int piv=row;
//            for(int i=row;i<m;i++) if (abs(M[i][col]).compareTo(abs(M[piv][col]))>0) piv=i;
//            if (isZero(M[piv][col])){ col++; continue; }
//            swap(M,row,piv);
//            Rat inv = Rat.div(Rat.of(1), M[row][col]);
//            for(int j=col;j<=n;j++) M[row][j]=Rat.mul(M[row][j], inv);
//            for(int i=0;i<m;i++){
//                if (i==row) continue;
//                Rat f = M[i][col];
//                if (isZero(f)) continue;
//                for(int j=col;j<=n;j++) M[i][j]=Rat.sub(M[i][j], Rat.mul(f, M[row][j]));
//            }
//            row++; col++;
//        }
//        var x = new Rat[n]; Arrays.fill(x, Rat.of(0));
//        for(int i=0;i<m;i++){
//            int lead = firstOne(M[i]);
//            if (lead==-1){ if (!isZero(M[i][n])) return Optional.empty(); else continue; }
//            x[lead]=M[i][n];
//        }
//        return Optional.of(x);
//    }
//    static int firstOne(Rat[] r){ for(int j=0;j<r.length-1;j++) if (isOne(r[j])) return j; return -1; }
//    static boolean isZero(Rat r){ return r.n==0; }
//    static boolean isOne(Rat r){ return r.n==1 && r.d==1; }
//    static Rat abs(Rat r){ return new Rat(Math.abs(r.n), r.d); }
//    static void swap(Rat[][] M,int a,int b){ var t=M[a]; M[a]=M[b]; M[b]=t; }
//}
//final class DimSolver {
//    record BalanceResult(boolean ok, Map<String,Rat> powers, String note) {}
//    static BalanceResult balance(Dim lhs, Dim rhs, List<PhysicalConstant> constants){
//        int[] delta = lhs.sub(rhs).v(); // want A x = -delta
//        int m=4, n=constants.size();
//        Rat[][] A = new Rat[m][n];
//        for(int i=0;i<m;i++) for(int j=0;j<n;j++){
//            int v = switch(i){case 0->constants.get(j).dim().L(); case 1->constants.get(j).dim().M();
//                case 2->constants.get(j).dim().T(); default->constants.get(j).dim().Q();};
//            A[i][j]= new Rat(v,1);
//        }
//        Rat[] b = new Rat[m];
//        for(int i=0;i<m;i++) b[i] = new Rat(-delta[i],1);
//
//        var sol = LinRat.solve(A,b);
//        if (sol.isEmpty()) return new BalanceResult(false, Map.of(), "no exact rational solution");
//
//        var x = sol.get();
//        var map = new LinkedHashMap<String,Rat>();
//        for(int j=0;j<n;j++){
//            if (x[j].n!=0){
//                var id = constants.get(j).id();
//                map.put(id==null?("KAPPA"+j):id.name(), x[j]);
//            }
//        }
//        return new BalanceResult(true, map, map.isEmpty()? "already balanced" : "use constants with powers");
//    }
//    static String pretty(Map<String,Rat> p){
//        if (p.isEmpty()) return "∅";
//        return p.entrySet().stream().map(e-> e.getKey()+"^("+e.getValue()+")").collect(Collectors.joining(" · "));
//    }
//}
//
///* ============== Expr utils & numeric evaluation ============== */
//final class ExprUtil {
//    static Set<String> varsIn(Expr e){
//        var out=new LinkedHashSet<String>();
//        walk(e, out); return out;
//    }
//    private static void walk(Expr e, Set<String> out){
//        if (e instanceof VarE v) out.add(v.v().name());
//        else if (e instanceof Unary u) walk(u.x(), out);
//        else if (e instanceof Binary b){ walk(b.a(), out); walk(b.b(), out); }
//    }
//}
//final class Evaluator {
//    interface Row { double get(String var); }
//    static double eval(Expr e, Row row){
//        if (e instanceof VarE v) return row.get(v.v().name());
//        if (e instanceof ConstE c){
//            var hit = ConstantSet.numeric(c.name());
//            return hit.isPresent() ? hit.getAsDouble() : parseMaybeRational(c.name());
//        }
//        if (e instanceof Unary u){
//            double x = eval(u.x(), row);
//            return switch(u.op()){
//                case NEG -> -x;
//                case SQRT -> Math.sqrt(x);
//                case EXP -> Math.exp(x);
//                case LOG -> Math.log(x);
//                case SIN -> Math.sin(x);
//                case COS -> Math.cos(x);
//            };
//        }
//        if (e instanceof Binary b){
//            double A=eval(b.a(),row), B=eval(b.b(),row);
//            return switch(b.op()){
//                case ADD -> A+B;
//                case SUB -> A-B;
//                case MUL -> A*B;
//                case DIV -> A/B;
//                case POW -> {
//                    if (b.b() instanceof ConstE cb){
//                        if (cb.name().equals("1/2")) yield Math.sqrt(A);
//                        if (cb.name().matches("-?\\d+")) yield Math.pow(A, Integer.parseInt(cb.name()));
//                    }
//                    yield Math.pow(A,B);
//                }
//            };
//        }
//        throw new IllegalStateException("Unknown expr");
//    }
//    private static double parseMaybeRational(String s){
//        if (s.contains("/")){
//            var p=s.split("/");
//            try { return Double.parseDouble(p[0]) / Double.parseDouble(p[1]); } catch(Exception ex){ return 1.0; }
//        }
//        try { return Double.parseDouble(s); } catch(Exception ex){ return 1.0; }
//    }
//}
//
///* ============== Symbolic enumeration (small algebra, constants allowed) ============== */
//record Template(Expr left, Expr right, double complexity, String rationale) {}
//final class SymbolicEnumerator {
//    static List<Template> enumerate(List<Variable> vars, int maxSize){
//        var constants = ConstantSet.all();
//        var pool = new ArrayList<Expr>();
//        for (var v: vars) pool.add(new VarE(v));
//        // allow named constants
//        for (var pc: constants) pool.add(new ConstE(pc.id()==null?"KAPPA":pc.id().name(), pc.nset(), pc.dim()));
//        pool.add(new ConstE("2", NumberSet.R, Dim.ONE())); pool.add(new ConstE("1/2", NumberSet.R, Dim.ONE()));
//
//        var exprs = new HashSet<Expr>(pool);
//        for (int s=2;s<=maxSize;s++){
//            var next=new HashSet<Expr>();
//            for (var e: exprs){
//                // unary
//                for (var op : UOp.values()){
//                    var c=new Unary(op,e);
//                    if (TypeRules.unaryOK(op,e)) next.add(c);
//                }
//                // binary
//                for (var f: exprs) for (var bop: BOp.values()){
//                    var c=new Binary(bop,e,f);
//                    if (TypeRules.binaryOK(bop,e,f)) next.add(c);
//                }
//            }
//            exprs.addAll(next);
//        }
//
//        var out = new ArrayList<Template>();
//        var list = new ArrayList<>(exprs);
//        for (var a: list){
//            for (var b: list){
//                // require dimensional compatibility (exact or balanceable by constants)
//                var bal = DimSolver.balance(a.dim(), b.dim(), constants);
//                if (!bal.ok()) continue;
//                String rationale = "Structure-compatible; Dim-balance: "+DimSolver.pretty(bal.powers());
//                out.add(new Template(a,b,a.size()+b.size(), rationale));
//            }
//        }
//        return out;
//    }
//}
//
///* ============== Empirical validator (LHS≈RHS) ============== */
//final class DataTable {
//    final Map<String,double[]> cols = new LinkedHashMap<>();
//    int rows(){ return cols.values().stream().findFirst().map(a->a.length).orElse(0); }
//    DataTable put(String var, double[] values){ cols.put(var, values); return this; }
//}
//final class Empirical {
//    static record FitScore(double mse, int nUsed) {}
//    static Optional<FitScore> score(Template tpl, DataTable data){
//        var need = new LinkedHashSet<String>();
//        need.addAll(ExprUtil.varsIn(tpl.left()));
//        need.addAll(ExprUtil.varsIn(tpl.right()));
//        if (!data.cols.keySet().containsAll(need)) return Optional.empty();
//
//        int n = data.rows();
//        double err=0; int used=0;
//        for(int i=0;i<n;i++){
//            int idx=i;
//            var row = (Evaluator.Row)(varName -> data.cols.get(varName)[idx]);
//            try{
//                double L = Evaluator.eval(tpl.left(), row);
//                double R = Evaluator.eval(tpl.right(), row);
//                double r = L - R;
//                if (Double.isFinite(r)){ err += r*r; used++; }
//            }catch(Exception ex){ return Optional.empty(); }
//        }
//        if (used==0) return Optional.empty();
//        return Optional.of(new FitScore(err/used, used));
//    }
//}
//
///* ============== Domain registry (variables + datasets) ============== */
//enum PhysicsDomain { QED, QM, GR, QCD }
//
//final class DomainConfig {
//    final PhysicsDomain domain;
//    final List<Variable> vars;
//    final DataTable data;
//    final List<EquationFamily> families; // light use; mostly to keep planner alive
//
//    DomainConfig(PhysicsDomain d, List<Variable> v, DataTable t, List<EquationFamily> f){
//        domain=d; vars=v; data=t; families=f;
//    }
//
//    static DomainConfig qed(){ // Electromagnetism plane wave: E = c B, x = c t
//        var E  = new Variable("E",  DataType.LIST, NumberSet.R, Util.setOf(Component.RF, Component.TIME, Component.SPACE), Dim.unitM().add(Dim.unitL()).sub(Dim.unitT().mul(2))); // V/m ≡ kg·m/(s^2·C)·C ??? (simplified mass-based form)
//        var B  = new Variable("B",  DataType.LIST, NumberSet.R, Util.setOf(Component.RF, Component.TIME, Component.SPACE), Dim.unitM().sub(Dim.unitT().mul(2))); // Tesla simplified
//        var t  = new Variable("t",  DataType.LIST, NumberSet.R, Util.setOf(Component.TIME), Dim.unitT());
//        var x  = new Variable("x",  DataType.LIST, NumberSet.R, Util.setOf(Component.SPACE, Component.TIME), Dim.unitL());
//        var vars = List.of(E,B,t,x);
//
//        int n=101; double[] tt=new double[n], xx=new double[n], EE=new double[n], BB=new double[n];
//        double c = ConstantSet.numeric("C_LIGHT").orElse(299_792_458.0);
//        double E0 = 3.0; double omega = 2.0;
//        for(int i=0;i<n;i++){
//            double time = i*1e-8;
//            tt[i]=time; xx[i]=c*time;
//            EE[i]=E0*Math.sin(omega*time);
//            BB[i]=EE[i]/c;
//        }
//        var dt = new DataTable().put("t",tt).put("x",xx).put("E",EE).put("B",BB);
//
//        var fams = List.of(
//                new EquationFamily("Linear Constitutive", new Sig(List.of(DataType.LIST), List.of(NumberSet.R)), new Sig(List.of(DataType.LIST), List.of(NumberSet.R)), Util.setOf(Component.RF,Component.SPACE,Component.TIME))
//        );
//
//        return new DomainConfig(PhysicsDomain.QED, vars, dt, fams);
//    }
//
//    static DomainConfig qm(){ // Free particle: E = ħ^2 k^2 / (2m)  (use y=E, inputs ħ, k, m)
//        var k  = new Variable("k",  DataType.LIST, NumberSet.R, Util.setOf(Component.SPACE), Dim.unitL().mul(-1)); // 1/m
//        var m  = new Variable("m",  DataType.LIST, NumberSet.R, Util.setOf(Component.MASS), Dim.unitM());          // kg
//        var E  = new Variable("E",  DataType.LIST, NumberSet.R, Util.setOf(Component.ENERGY), Dim.ENERGY());   // J
//        var vars = List.of(k,m,E);
//
//        int n=30; double[] kk=new double[n], mm=new double[n], EE=new double[n];
//        double hbar = ConstantSet.numeric("HBAR").orElse(1.054e-34);
//        Random r = new Random(0);
//        for(int i=0;i<n;i++){
//            double mval = 9.11e-31 + (i%3)*1e-32; // three masses
//            double kval = 1e10 + i*5e8;
//            kk[i]=kval; mm[i]=mval;
//            double Ei = (hbar*hbar*kval*kval)/(2.0*mval);
//            EE[i]=Ei*(1.0+1e-6*(r.nextDouble()-0.5)); // tiny noise
//        }
//        var dt = new DataTable().put("k",kk).put("m",mm).put("E",EE);
//
//        var fams = List.of(new EquationFamily("Quadratic Dispersion", new Sig(List.of(DataType.LIST), List.of(NumberSet.R)), new Sig(List.of(DataType.LIST), List.of(NumberSet.R)), Util.setOf(Component.ENERGY)));
//        return new DomainConfig(PhysicsDomain.QM, vars, dt, fams);
//    }
//
//    static DomainConfig gr(){ // Weak field redshift: z ≈ g h / c^2  (dimensionless)
//        var z  = new Variable("z",  DataType.LIST, NumberSet.R, Util.setOf(Component.TIME,Component.SPACE), Dim.ONE());
//        var g  = new Variable("g",  DataType.LIST, NumberSet.R, Util.setOf(Component.SPACE,Component.TIME), Dim.unitL().sub(Dim.unitT().mul(2)));
//        var h  = new Variable("h",  DataType.LIST, NumberSet.R, Util.setOf(Component.SPACE), Dim.unitL());
//        var vars = List.of(z,g,h);
//
//        int n=11; double[] hh=new double[n], gg=new double[n], zz=new double[n];
//        double c = ConstantSet.numeric("C_LIGHT").orElse(299_792_458.0);
//        double g0=9.80665;
//        for(int i=0;i<n;i++){
//            double hi = i*10.0; // meters
//            hh[i]=hi; gg[i]=g0;
//            zz[i]= g0*hi/(c*c);
//        }
//        var dt = new DataTable().put("h",hh).put("g",gg).put("z",zz);
//
//        var fams = List.of(new EquationFamily("Weak-field GR", new Sig(List.of(DataType.LIST), List.of(NumberSet.R)), new Sig(List.of(DataType.LIST), List.of(NumberSet.R)), Util.setOf(Component.TIME,Component.SPACE)));
//        return new DomainConfig(PhysicsDomain.GR, vars, dt, fams);
//    }
//
//    static DomainConfig qcd(){ // Running coupling: α_s(Q) ≈ 1/(β0 ln(Q^2/Λ^2)), β0=(33-2n_f)/12π (dimensionless)
//        var Q  = new Variable("Q",  DataType.LIST, NumberSet.R, Util.setOf(Component.ENERGY), Dim.ENERGY()); // treat as energy scale (J)
//        var aS = new Variable("alpha_s", DataType.LIST, NumberSet.R, Util.setOf(Component.EMPTY_GEOMETRY), Dim.ONE());
//        var vars = List.of(Q,aS);
//
//        int n=40; double[] QQ=new double[n], AS=new double[n];
//        double pi = Math.PI;
//        int nf=5;
//        double beta0 = (33.0 - 2.0*nf)/(12.0*pi);
//        double Lambda = 0.2; // GeV (we'll work dimensionless by dividing Q by GeV)
//        for(int i=0;i<n;i++){
//            double qGeV = 1.0 + i*0.2;
//            QQ[i]= qGeV; // store in "GeV units" (dimensionless stand-in)
//            AS[i]= 1.0/(beta0*Math.log((qGeV*qGeV)/(Lambda*Lambda)));
//        }
//        var dt = new DataTable().put("Q",QQ).put("alpha_s",AS);
//
//        var fams = List.of(new EquationFamily("Asymptotic Freedom", new Sig(List.of(DataType.LIST), List.of(NumberSet.R)), new Sig(List.of(DataType.LIST), List.of(NumberSet.R)), Util.setOf(Component.EMPTY_GEOMETRY)));
//        return new DomainConfig(PhysicsDomain.QCD, vars, dt, fams);
//    }
//}
//
///* ============== Discovery planner & printer (lite) ============== */
//final class DiscoveryPlanner {
//    record Report(List<Binder.Binding> allBindings, Planner.Plan plan) {}
//    static Report run(List<Variable> vars, List<EquationFamily> catalog){
//        var all = new ArrayList<Binder.Binding>();
//        for (var fam: catalog){
//            all.addAll(Binder.explore(fam, vars));
//        }
//        var plan = Planner.choose(new ArrayList<>(all), vars, catalog);
//        return new Report(all, plan);
//    }
//}
//final class PlanPrinter {
//    static void print(DiscoveryPlanner.Report report){
//        System.out.println("============== PLAN ==============");
//        System.out.println("Total score (+grammar): " + Util.fmt(report.plan().totalScore()));
//        System.out.println();
//        int i=1;
//        for (var ch : report.plan().chosen()){
//            System.out.println("[" + i + "] " + ch.binding().fam.family());
//            System.out.print(ch.explanation());
//            System.out.println();
//            i++;
//        }
//    }
//}
//
///* ============== Runner ============== */
//public class ExistenceEquationDiscovery {
//
//    private static List<Template> makeTemplates(List<Variable> vars, int maxSize){
//        // Small, pragmatic search focused on simple algebra with constants.
//        return SymbolicEnumerator.enumerate(vars, maxSize);
//    }
//
//    private static void tryDomain(DomainConfig cfg){
//        System.out.println("\n============== DOMAIN: " + cfg.domain + " ==============");
//        // Tiny “catalog” so the planner has something to score
//        var report = DiscoveryPlanner.run(cfg.vars, cfg.families);
//        PlanPrinter.print(report);
//
//        // Enumerate expressions
//        var cands = makeTemplates(cfg.vars, 3); // size 3 keeps runtime snappy
//
//        // Empirical check
//        System.out.println("=== Empirical equation check (LHS ≈ RHS) ===");
//        int hits=0;
//        for (var tpl : cands){
//            var fit = Empirical.score(tpl, cfg.data);
//            if (fit.isPresent()){
//                // Set domain-specific tolerances
//                double thr = switch (cfg.domain){
//                    case QED, QM -> 1e-12;
//                    case GR      -> 1e-24;
//                    case QCD     -> 1e-6;
//                };
//                if (fit.get().mse() < thr){
//                    System.out.println("OK: "+tpl.left()+" = "+tpl.right()
//                            +"   mse="+Util.fmt(fit.get().mse())+"   n="+fit.get().nUsed()
//                            +"   {" + tpl.rationale() + "}");
//                    if (++hits>=8) break;
//                }
//            }
//        }
//        if (hits==0) System.out.println("No candidate passed threshold — raise search size or adjust tolerances.");
//    }
//
//    public static void main(String[] args){
//        // Domains + datasets (synthetic but faithful shape)
//        var domains = List.of(
//                DomainConfig.qed(),
//                DomainConfig.qm(),
//                DomainConfig.gr(),
//                DomainConfig.qcd()
//        );
//        for (var d : domains) tryDomain(d);
//
//        System.out.println("\nDone. Equations only “exist” if they are dimensionally consistent AND match the data.");
//    }
//}
