package com.markussecundus.forms.utils.vector;

import com.markussecundus.forms.utils.datastruct.ReadonlyList;
import com.markussecundus.forms.utils.function.Function;

import java.io.Serializable;


/**
 * Základní implementace immutable 2D vektoru s <code>double</code>ovými hodnotami.
 *
 * @see Vect2i
 * @see VectUtil
 *
 * @author MarkusSecundus
 * */
public class Vect2d implements Cloneable, Serializable, ReadonlyList.Double {

    /**
     * Souřadnice vektoru.
     * */
    public final double x,y;

    /**
     * @return vektor s požadovanými hodnotami převedenými na typ <code>double</code>.
     * Nemusí nutně jít o novou instanci.
     * */
    public static Vect2d make(double x, double y){return new Vect2d(x, y);}

    /**
     * @return sama sebe.
     * */
    public static Vect2d make(Vect2d v){return v;}

    /**
     * @return vektor s hodnotami odpovídajícími prvním dvěma prvkům v poli.
     * Nemusí nutně jít o novou instanci.
     * */
    public static Vect2d make(java.lang.Double[] arr) throws VectDecomposer.InconsistentNumberOfDimensionsException{
        if(arr.length<2) throw new VectDecomposer.InconsistentNumberOfDimensionsException();
        return make(arr[0], arr[1]);
    }

    /**
     * @return vektor s požadovanými hodnotami převedenými na typ <code>double</code>.
     * Nemusí nutně jít o novou instanci.
     * */
    public static Vect2d make(Vect2i v){return make(v.x, v.y);}

    public static Vect2d make(Vect2f v){return make(v.x, v.y);}

    private Vect2d(double x, double y){this.x = x;this.y = y;}

    /**
     * @return sama sebe.
     * */
    public Vect2d cpy(){return make(this);}


    /**
     * @return instanci vektoru s opačnými hodnotami.
     * */
    public Vect2d neg(){return make(-x, -y);}

    /**
     * @return převod tohoto vektoru na celočíselnou variantu.
     * (viz {@link Vect2i})
     * */
    public Vect2i toInt(){return Vect2i.make(this);}

    /**
     * @return převod tohoto vektoru na méně přesnou variantu.
     * (viz {@link Vect2i})
     * */
    public Vect2f toFloat(){return Vect2f.make(this);}

    /**
     * @return součet tohoto vektoru s danou hodnotou.
     * */
    public Vect2d add(double x, double y){return make(this.x + x, this.y+y);}
    /**
     * @return součet tohoto vektoru s danou hodnotou.
     * */
    public Vect2d add(Vect2d v){return add(v.x,v.y);}

    /**
     * @return rozdíl tohoto vektoru s danou hodnotou.
     * */
    public Vect2d sub(double x, double y){return make(this.x - x, this.y-y);}
    /**
     * @return rozdíl tohoto vektoru s danou hodnotou.
     * */
    public Vect2d sub(Vect2d v){return sub(v.x, v.y);}

    /**
     * @return součet tohoto vektoru s danou hodnotou souřadnice X.
     * */
    public Vect2d addX(double x){return make(this.x+x,y);}
    /**
     * @return součet tohoto vektoru s danou hodnotou souřadnice Y.
     * */
    public Vect2d addY(double y){return make(this.x,this.y+y);}
    /**
     * @return rozdíl tohoto vektoru s danou hodnotou souřadnice X.
     * */
    public Vect2d subX(double x){return make(this.x-x,y);}
    /**
     * @return rozdíl tohoto vektoru s danou hodnotou souřadnice Y.
     * */
    public Vect2d subY(double y){return make(this.x,this.y-y);}

    /**
     * @return násobek daného vektoru.
     * */
    public Vect2d scl(double f){return make(x*f, y*f);}

    /**
     * @return převrácený násobek daného vektoru.
     * */
    public Vect2d div(double f){return make(x/f, y/f);}

    /**
     * @return vektor s vynásobenou hodnotou na souřadnici X.
     * */
    public Vect2d sclX(double f){return make(x*f, y);}

    /**
     * @return vektor s vynásobenou hodnotou na souřadnici Y.
     * */
    public Vect2d sclY(double f){return make(x, y*f);}

    /**
     * @return <code>true</code> pokud je this < v v obou složkách
     * */
    public boolean lt(Vect2d v){return x<v.x && y<v.y;}

    /**
     * @return <code>true</code> pokud je this <= v v obou složkách
     * */
    public boolean le(Vect2d v){return x<=v.x && y<=v.y;}

    /**
     * @return <code>true</code> pokud je this > v v obou složkách
     * */
    public boolean gt(Vect2d v){return x>v.x && y>v.y;}

    /**
     * @return <code>true</code> pokud je this >= v v obou složkách
     * */
    public boolean ge(Vect2d v){return x>=v.x && y>=v.y;}

    /**
     * @return vektor takový, že jeho jednotlivé složky jsou zachovány, pokud jsou (neostře) pod hranicí
     * danou složkami parametru <code>bound</code>, jinak jsou nahrazey příslušnými hraničními hodnotami.
     * */
    public Vect2d withCeiling(Vect2d bound){
        return le(bound)?this: (ge(bound)? bound : make(Math.min(x, bound.x), Math.min(y, bound.y)));
    }
    /**
     * @return vektor takový, že jeho jednotlivé složky jsou zachovány, pokud jsou (neostře) nad hranicí
     * danou složkami parametru <code>bound</code>, jinak jsou nahrazey příslušnými hraničními hodnotami.
     * */
    public Vect2d withFloor(Vect2d bound){
        return ge(bound)?this: (le(bound)? bound : make(Math.max(x, bound.x), Math.max(y, bound.y)));
    }

    /**
     * @return původní vektor se změněnou složkou X.
     * */
    public Vect2d withX(double x){return x==this.x?this:make(x,y);}

    /**
     * @return původní vektor se změněnou složkou Y.
     * */
    public Vect2d withY(double y){return y==this.y?this:make(x,y);}

    /**
     * Vrátí 2. mocninu délky vektoru.
     * Rychlejší než počítat skutečnou délku a ke vzájemnému porovnávání délek slouží stejně dobře.
     *
     * @return 2. mocina délky vektoru
     * */
    public double len2(){return x*x + y*y;}

    /**
     * @return délka vektoru
     * */
    public double len(){return (double)Math.sqrt(len2());}

    /**
     * Vrátí 2. mocninu vzdálenosti od daného vektoru.
     * Rychlejší než počítat skutečnou délku a ke vzájemnému porovnávání vzdáleností slouží stejně dobře.
     *
     * @return 2. mocina vzdálenosti od vstupního vektoru
     * */
    public double dst2(double x, double y){
        return (x=this.x-x)*x + (y=this.y-y)*y;
    }

    /**
     * Vrátí 2. mocninu vzdálenosti od daného vektoru.
     * Rychlejší než počítat skutečnou délku a ke vzájemnému porovnávání vzdáleností slouží stejně dobře.
     *
     * @return 2. mocina vzdálenosti od vstupního vektoru
     * */
    public double dst2(Vect2d v){return dst2(v.x, v.y);}

    /**
     * @return vzdálenost od vstupního vektoru
     * */
    public double dst(double x, double y){return (double)Math.sqrt(dst2(x,y));}
    /**
     * @return vzdálenost od vstupního vektoru
     * */
    public double dst(Vect2d v){return dst(v.x, v.y);}

    /**
     * @return minimum z obou souřadnic
     * */
    public double min(){return Math.min(x,y);}

    /**
     * @return maximum z obou souřadnic
     * */
    public double max(){return Math.max(x,y);}

    /**
     * @return aritmetický průměr z obou souřadnic
     * */
    public double avg(){return (x+y)/2f;}

    /**
     * Vektor odpovídající souřadnicím (0f,0f)^T
     * */
    public static final Vect2d ZERO = make(0,0);

    /**
     * Vektor odpovídající souřadnicím (+Inf, +Inf)^T
     * */
    public static final Vect2d INF = make(java.lang.Double.POSITIVE_INFINITY, java.lang.Double.POSITIVE_INFINITY);

    @Override
    public int hashCode() {
        return (int)((1 + java.lang.Double.doubleToLongBits(x))*java.lang.Double.doubleToLongBits(x) + java.lang.Double.doubleToLongBits(y));
    }

    /**
     * @return <code>true</code> pokud jsou obě souřadnice vektoru naprosto stejné
     * */
    protected boolean equals(Vect2d v){
        return v==this || (v!=null && v.x == x && v.y == y);
    }

    /**
     * Shoda s liboovlnou instancí {@link Vect2d}, pro kterou <code>equals(Vect2f)</code> vrátí <code>true</code>.
     *
     * {@inheritDoc}
     * */
    @Override public boolean equals(Object obj) {
        return obj instanceof Vect2d && ((Vect2d)obj).equals(this);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return Vect2d.make(this);
        }
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)^T", x,y);
    }



    @Override
    public int size() { return 2; }

    @Override
    public double getNth_raw(int n) { return n==0? x: y; }



    /**
     * Rozloží vektor na pole složek.
     * */
    public java.lang.Double[] decompose(){return new java.lang.Double[]{x,y};}

    /**
     * Složí vektor z pole složek.
     * */
    public static Vect2d compose(java.lang.Double[] arr){return make(arr);}

    /**
     * Vrátí instanci příslušné {@link VectUtil}.
     * */
    public static VectUtil<Vect2d, java.lang.Double> getUtility(){return Util.INSTANCE;}


    /**
     * Utilita pro typ {@link Vect2d}.
     *
     * @see VectUtil
     *
     * @author MarkusSecundus
     * */
    public static class Util implements VectUtil.BasicImplementations.VectorUtil_DoubleAsScalar<Vect2d> {
        protected Util(){}

        /**
         * Jediná existující instance.
         * */
        public static final Util INSTANCE = new Util();

        public Vect2d add(Vect2d a, Vect2d b){return a.add(b);}
        public Vect2d sub(Vect2d a, Vect2d b){return a.sub(b);}

        public Vect2d scl(Vect2d a, java.lang.Double b){return a.scl(b);}
        public Vect2d div(Vect2d a, java.lang.Double b){return a.div(b);}

        public Vect2d scl(Vect2d a, double b) { return a.scl((double)b); }
        public Vect2d div(Vect2d a, double b) { return a.div((double)b); }


        public java.lang.Double len2(Vect2d v){return v.len2();}
        public java.lang.Double len(Vect2d v){return v.len();}


        public java.lang.Double dst2(Vect2d a, Vect2d b){return a.dst2(b);}
        public java.lang.Double dst(Vect2d a, Vect2d b){return a.dst(b);}


        public boolean lt(Vect2d a, Vect2d b){return a.x < b.x && a.y < b.y;}
        public boolean le(Vect2d a, Vect2d b){return a.x <= b.x && a.y <= b.y;}


        public Vect2d cpy(Vect2d v){return v;}


        public java.lang.Double minScalar(Vect2d v) { return v.min(); }
        public java.lang.Double maxScalar(Vect2d v) { return v.max(); }


        public java.lang.Double[] decompose(Vect2d p) { return p.decompose(); }
        public Vect2d compose(java.lang.Double... s) { return Vect2d.compose(s); }

        public int DIMENSION_COUNT(){return 2;}


        public java.lang.Double getNth(Vect2d v, int n) { return v.getNth(n); }
        public Vect2d withNth(Vect2d p, int n, java.lang.Double s) { return n==0?make(s, p.y):make(p.x, s); }


        public Vect2d ZERO(){return ZERO;}


        public Vect2d MAX_VAL(){return INF;}


        @Override public Class<Vect2d> getVectClass() { return Vect2d.class; }
    }

    /**
     * Transformační funkce, která k oběma složkám vektoru přičítá stejnou konstantní hodnotu.
     * Immutable.
     *
     * @see com.markussecundus.forms.gfx.BinaryGraphicalComposite
     *
     * @author MarkusSecundus
     * */
    public static class DoubleAdder implements Function<Vect2d, Vect2d>{
        public DoubleAdder(double ammount){this.ammount = ammount;}

        /**
         * Provede transformaci.
         *
         * @return vektor se všemi složkami zvýšenými o <code>ammount</code>
         * */
        public Vect2d apply(Vect2d v) { return v.add(ammount, ammount); }

        /**
         * Hodnota přičítaná k vektoru.
         * */
        public final double ammount;

        /**
         * @return instance s danou hodnotou <code>ammount</code>
         * */
        public static DoubleAdder make(double ammount){return new DoubleAdder(ammount);}

        /**
         * @return instance s přičítanou hodnotou zvýšenou o parametr funkce
         * */
        public DoubleAdder add(double ammount){return new DoubleAdder(this.ammount + ammount);}
    }
    /**
     * Transformační funkce, která obě složky vektoru násobí stejnou konstantní hodnotou.
     * Immutable.
     *
     * @see com.markussecundus.forms.gfx.BinaryGraphicalComposite
     *
     * @author MarkusSecundus
     * */
    public static class DoubleMultiplier implements Function<Vect2d, Vect2d>{
        public DoubleMultiplier(double ammount){this.ammount = ammount;}

        /**
         * Provede transformaci.
         *
         * @return vektor vynásobený hodnotou <code>ammount</code>
         * */
        public Vect2d apply(Vect2d v) { return v.scl(ammount); }

        /**
         * Hodnota, kterou je vektor násoben.
         * */
        public final double ammount;

        /**
         * @return instance s danou hodnotou <code>ammount</code>
         * */
        public static DoubleMultiplier make(double ammount){return new DoubleMultiplier(ammount);}

        /**
         * @return instance s násobící hodnotou zvýšenou o parametr funkce
         * */
        public DoubleMultiplier add(double ammount){return new DoubleMultiplier(this.ammount + ammount);}
    }
    /**
     * Transformační funkce, která k vektoru přičítá konstantní hodnotu.
     * Immutable.
     *
     * @see com.markussecundus.forms.gfx.BinaryGraphicalComposite
     *
     * @author MarkusSecundus
     * */
    public static class Adder implements Function<Vect2d, Vect2d>{
        public Adder(Vect2d ammount){this.ammount = ammount;}

        /**
         * Provede transformaci.
         *
         * @return součet vstupní hodnoty s hodnotou <code>ammount</code>
         * */
        public Vect2d apply(Vect2d v) { return v.add(ammount); }
        public final Vect2d ammount;

        /**
         * @return instance s danou hodnotou <code>ammount</code>
         * */
        public static Adder make(Vect2d ammount){return new Adder(ammount);}
        /**
         * @return instance s přičítanou hodnotou zvýšenou o parametr funkce
         * */
        public Adder add(Vect2d ammount){return new Adder(this.ammount.add(ammount));}
        /**
         * @return instance s přičítanou hodnotou zvýšenou o parametr funkce
         * */
        public Adder add(double x, double y){return new Adder(this.ammount.add(x, y));}
    }


    /**
     * Transformační funkce, která k vektoru přičítá hodnotu získanou jako podíl z jeho velikosti.
     * Immutable.
     *
     * @see com.markussecundus.forms.gfx.BinaryGraphicalComposite
     *
     * @author MarkusSecundus
     * */
    public static class ExtractMultAdder implements Function<Vect2d, Vect2d>{

        /**
         * @param ratio poměr, kterým bude pronásobena výsledná hodnota
         * @param extractor funkce, která z vektoru vytáhne číselnou hodnotu, jenž bude následně násobena poměrem
         * */
        public ExtractMultAdder(double ratio, Function<Vect2d, java.lang.Double> extractor){
            this.ratio = ratio;
            this.extractor = extractor;
        }

        /**
         * Provede transformaci.
         *
         * @return přetransformuje vstupní argument skrze <code>extractor</code> a vynásobí ho <code>ratio</code>.
         * */
        public Vect2d apply(Vect2d v){
            double toBeAdded = extractor.apply(v)*ratio;
            return v.add(toBeAdded, toBeAdded);
        }

        /**
         * Funkce, která z vektoru vytáhuje číselnou hodnotu, k násobení poměrem.
         */
        public final Function<Vect2d, java.lang.Double> extractor;
        /**
         * Poměr, k pronásobení výsledné hodnoty.
         * */
        public final double ratio;

        /**
         * @return instance s danou hodnotou <code>ammount</code>
         * */
        public static ExtractMultAdder make(double ratio, Function<Vect2d, java.lang.Double> extractor){return new ExtractMultAdder(ratio, extractor);}

        /**
         * @return instance s danou hodnotou <code>ratio</code> a zachovanou transformační funkcí
         * */
        public ExtractMultAdder with(double ratio){return make(ratio, extractor);}
        /**
         * @return instance s danou transformační funkcí a zachovanou hodnotou <code>ratio</code>
         * */
        public ExtractMultAdder with(Function<Vect2d, java.lang.Double> extractor){return make(ratio, extractor);}
    }

    /**
     * Factory na {@link DoubleAdder}.
     *
     * @return instance {@link DoubleAdder} s danou hodnotou
     * */
    public static DoubleAdder makeAdder(double ammount){return DoubleAdder.make(ammount);}

    /**
     * Factory na {@link Adder}.
     *
     * @return instance {@link Adder} s danou hodnotou
     * */
    public static Adder makeAdder(Vect2d ammount){return Adder.make(ammount);}

    /**
     * Factory na {@link DoubleMultiplier}.
     *
     * @return instance {@link DoubleMultiplier} s danou hodnotou
     * */
    public static DoubleMultiplier makeMultiplier(double ammount){return DoubleMultiplier.make(ammount);}

    /**
     * Factory na {@link ExtractMultAdder}.
     *
     * @return instance {@link ExtractMultAdder} s danou hodnotou
     * */
    public static ExtractMultAdder makeRatioAdder(double ratio, Function<Vect2d, java.lang.Double> extractor){return ExtractMultAdder.make(ratio, extractor);}


    /**
     * Vytáhne z vektoru jeho minimum.
     *
     * Může sloužit jako parametr pro {@link ExtractMultAdder}.
     * */
    public static final Function<Vect2d, java.lang.Double> minFunc = Vect2d::min;

    /**
     * Vytáhne z vektoru jeho maximum.
     *
     * Může sloužit jako parametr pro {@link ExtractMultAdder}.
     * */
    public static final Function<Vect2d, java.lang.Double> maxFunc = Vect2d::max;

    /**
     * Vytáhne z vektoru průměr jeho složek.
     *
     * Může sloužit jako parametr pro {@link ExtractMultAdder}.
     * */
    public static final Function<Vect2d, java.lang.Double> avgFunc = Vect2d::avg;
}
