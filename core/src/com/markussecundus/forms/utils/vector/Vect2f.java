package com.markussecundus.forms.utils.vector;

import com.markussecundus.forms.utils.datastruct.ReadonlyList;
import com.markussecundus.forms.utils.function.Function;

import java.io.Serializable;


/**
 * Základní implementace immutable 2D vektoru s <code>float</code>ovými hodnotami.
 *
 * @see Vect2i
 * @see VectUtil
 *
 * @author MarkusSecundus
 * */
public class Vect2f implements Cloneable, Serializable, ReadonlyList.Float {

    /**
     * Souřadnice vektoru.
     * */
    public final float x,y;

    /**
     * @return vektor s požadovanými hodnotami.
     * Nemusí nutně jít o novou instanci.
     * */
    public static Vect2f make(float x, float y){return new Vect2f(x,y);}

    /**
     * @return vektor s požadovanými hodnotami převedenými na typ <code>float</code>.
     * Nemusí nutně jít o novou instanci.
     * */
    public static Vect2f make(double x, double y){return make((float)x, (float)y);}

    /**
     * @return sama sebe.
     * */
    public static Vect2f make(Vect2f v){return v;}

    /**
     * @return vektor s hodnotami odpovídajícími prvním dvěma prvkům v poli.
     * Nemusí nutně jít o novou instanci.
     * */
    public static Vect2f make(java.lang.Float[] arr) throws VectDecomposer.InconsistentNumberOfDimensionsException{
        if(arr.length<2) throw new VectDecomposer.InconsistentNumberOfDimensionsException();
        return make(arr[0], arr[1]);
    }

    /**
     * @return vektor s požadovanými hodnotami převedenými na typ <code>float</code>.
     * Nemusí nutně jít o novou instanci.
     * */
    public static Vect2f make(Vect2i v){return make(v.x, v.y);}

    public static Vect2f make(Vect2d v){return make(v.x, v.y);}

    private Vect2f(float x, float y){this.x = x;this.y = y;}

    /**
     * @return sama sebe.
     * */
    public Vect2f cpy(){return make(this);}


    /**
     * @return instanci vektoru s opačnými hodnotami.
     * */
    public Vect2f neg(){return make(-x, -y);}

    /**
     * @return převod tohoto vektoru na celočíselnou variantu.
     * (viz {@link Vect2i})
     * */
    public Vect2i toInt(){return Vect2i.make(this);}

    /**
     * @return převod tohoto vektoru na přesnější variantu.
     * (viz {@link Vect2d})
     * */
    public Vect2d toDouble(){return Vect2d.make(this);}

    /**
     * @return součet tohoto vektoru s danou hodnotou.
     * */
    public Vect2f add(float x, float y){return make(this.x + x, this.y+y);}
    /**
     * @return součet tohoto vektoru s danou hodnotou.
     * */
    public Vect2f add(Vect2f v){return add(v.x,v.y);}

    /**
     * @return rozdíl tohoto vektoru s danou hodnotou.
     * */
    public Vect2f sub(float x, float y){return make(this.x - x, this.y-y);}
    /**
     * @return rozdíl tohoto vektoru s danou hodnotou.
     * */
    public Vect2f sub(Vect2f v){return sub(v.x, v.y);}

    /**
     * @return součet tohoto vektoru s danou hodnotou souřadnice X.
     * */
    public Vect2f addX(float x){return make(this.x+x,y);}
    /**
     * @return součet tohoto vektoru s danou hodnotou souřadnice Y.
     * */
    public Vect2f addY(float y){return make(this.x,this.y+y);}
    /**
     * @return rozdíl tohoto vektoru s danou hodnotou souřadnice X.
     * */
    public Vect2f subX(float x){return make(this.x-x,y);}
    /**
     * @return rozdíl tohoto vektoru s danou hodnotou souřadnice Y.
     * */
    public Vect2f subY(float y){return make(this.x,this.y-y);}

    /**
     * @return násobek daného vektoru.
     * */
    public Vect2f scl(float f){return make(x*f, y*f);}

    /**
     * @return převrácený násobek daného vektoru.
     * */
    public Vect2f div(float f){return make(x/f, y/f);}

    /**
     * @return vektor s vynásobenou hodnotou na souřadnici X.
     * */
    public Vect2f sclX(float f){return make(x*f, y);}

    /**
     * @return vektor s vynásobenou hodnotou na souřadnici Y.
     * */
    public Vect2f sclY(float f){return make(x, y*f);}

    /**
     * @return <code>true</code> pokud je this < v v obou složkách
     * */
    public boolean lt(Vect2f v){return x<v.x && y<v.y;}

    /**
     * @return <code>true</code> pokud je this <= v v obou složkách
     * */
    public boolean le(Vect2f v){return x<=v.x && y<=v.y;}

    /**
     * @return <code>true</code> pokud je this > v v obou složkách
     * */
    public boolean gt(Vect2f v){return x>v.x && y>v.y;}

    /**
     * @return <code>true</code> pokud je this >= v v obou složkách
     * */
    public boolean ge(Vect2f v){return x>=v.x && y>=v.y;}

    /**
     * @return vektor takový, že jeho jednotlivé složky jsou zachovány, pokud jsou (neostře) pod hranicí
     * danou složkami parametru <code>bound</code>, jinak jsou nahrazey příslušnými hraničními hodnotami.
     * */
    public Vect2f withCeiling(Vect2f bound){
        return le(bound)?this: (ge(bound)? bound : make(Math.min(x, bound.x), Math.min(y, bound.y)));
    }
    /**
     * @return vektor takový, že jeho jednotlivé složky jsou zachovány, pokud jsou (neostře) nad hranicí
     * danou složkami parametru <code>bound</code>, jinak jsou nahrazey příslušnými hraničními hodnotami.
     * */
    public Vect2f withFloor(Vect2f bound){
        return ge(bound)?this: (le(bound)? bound : make(Math.max(x, bound.x), Math.max(y, bound.y)));
    }

    /**
     * @return původní vektor se změněnou složkou X.
     * */
    public Vect2f withX(float x){return x==this.x?this:make(x,y);}

    /**
     * @return původní vektor se změněnou složkou Y.
     * */
    public Vect2f withY(float y){return y==this.y?this:make(x,y);}

    /**
     * Vrátí 2. mocninu délky vektoru.
     * Rychlejší než počítat skutečnou délku a ke vzájemnému porovnávání délek slouží stejně dobře.
     *
     * @return 2. mocina délky vektoru
     * */
    public float len2(){return x*x + y*y;}

    /**
     * @return délka vektoru
     * */
    public float len(){return (float)Math.sqrt(len2());}

    /**
     * Vrátí 2. mocninu vzdálenosti od daného vektoru.
     * Rychlejší než počítat skutečnou délku a ke vzájemnému porovnávání vzdáleností slouží stejně dobře.
     *
     * @return 2. mocina vzdálenosti od vstupního vektoru
     * */
    public float dst2(float x, float y){
        return (x=this.x-x)*x + (y=this.y-y)*y;
    }

    /**
     * Vrátí 2. mocninu vzdálenosti od daného vektoru.
     * Rychlejší než počítat skutečnou délku a ke vzájemnému porovnávání vzdáleností slouží stejně dobře.
     *
     * @return 2. mocina vzdálenosti od vstupního vektoru
     * */
    public float dst2(Vect2f v){return dst2(v.x, v.y);}

    /**
     * @return vzdálenost od vstupního vektoru
     * */
    public float dst(float x, float y){return (float)Math.sqrt(dst2(x,y));}
    /**
     * @return vzdálenost od vstupního vektoru
     * */
    public float dst(Vect2f v){return dst(v.x, v.y);}

    /**
     * @return minimum z obou souřadnic
     * */
    public float min(){return Math.min(x,y);}

    /**
     * @return maximum z obou souřadnic
     * */
    public float max(){return Math.max(x,y);}

    /**
     * @return aritmetický průměr z obou souřadnic
     * */
    public float avg(){return (x+y)/2f;}

    /**
     * Vektor odpovídající souřadnicím (0f,0f)^T
     * */
    public static final Vect2f ZERO = make(0,0);

    /**
     * Vektor odpovídající souřadnicím (+Inf, +Inf)^T
     * */
    public static final Vect2f INF = make(java.lang.Float.POSITIVE_INFINITY, java.lang.Float.POSITIVE_INFINITY);

    @Override
    public int hashCode() {
        return (1 + java.lang.Float.floatToIntBits(x))*java.lang.Float.floatToIntBits(x) + java.lang.Float.floatToIntBits(y);
    }

    /**
     * @return <code>true</code> pokud jsou obě souřadnice vektoru naprosto stejné
     * */
    protected boolean equals(Vect2f v){
        return v==this || (v!=null && v.x == x && v.y == y);
    }

    /**
     * Shoda s liboovlnou instancí {@link Vect2f}, pro kterou <code>equals(Vect2f)</code> vrátí <code>true</code>.
     *
     * {@inheritDoc}
     * */
    @Override public boolean equals(Object obj) {
        return obj instanceof Vect2f && ((Vect2f)obj).equals(this);
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return Vect2f.make(this);
        }
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)^T", x,y);
    }



    @Override
    public int size() { return 2; }

    @Override
    public float getNth_raw(int n) { return n==0? x: y; }



    /**
     * Rozloží vektor na pole složek.
     * */
    public java.lang.Float[] decompose(){return new java.lang.Float[]{x,y};}

    /**
     * Složí vektor z pole složek.
     * */
    public static Vect2f compose(java.lang.Float[] arr){return make(arr);}

    /**
     * Vrátí instanci příslušné {@link VectUtil}.
     * */
    public static VectUtil<Vect2f, java.lang.Float> getUtility(){return Util.INSTANCE;}


    /**
     * Utilita pro typ {@link Vect2f}.
     *
     * @see VectUtil
     *
     * @author MarkusSecundus
     * */
    public static class Util implements VectUtil.BasicImplementations.VectorUtil_FloatAsScalar<Vect2f> {
        protected Util(){}

        /**
         * Jediná existující instance.
         * */
        public static final Util INSTANCE = new Util();

        public  Vect2f add(Vect2f a, Vect2f b){return a.add(b);}
        public  Vect2f sub(Vect2f a, Vect2f b){return a.sub(b);}

        public  Vect2f scl(Vect2f a, java.lang.Float b){return a.scl(b);}
        public  Vect2f div(Vect2f a, java.lang.Float b){return a.div(b);}

        public Vect2f scl(Vect2f a, double b) { return a.scl((float)b); }
        public Vect2f div(Vect2f a, double b) { return a.div((float)b); }


        public java.lang.Float len2(Vect2f v){return v.len2();}
        public java.lang.Float len(Vect2f v){return v.len();}


        public java.lang.Float dst2(Vect2f a, Vect2f b){return a.dst2(b);}
        public java.lang.Float dst(Vect2f a, Vect2f b){return a.dst(b);}


        public boolean lt(Vect2f a, Vect2f b){return a.x < b.x && a.y < b.y;}
        public boolean le(Vect2f a, Vect2f b){return a.x <= b.x && a.y <= b.y;}


        public Vect2f cpy(Vect2f v){return v;}


        public java.lang.Float minScalar(Vect2f v) { return v.min(); }
        public java.lang.Float maxScalar(Vect2f v) { return v.max(); }


        public java.lang.Float[] decompose(Vect2f p) { return p.decompose(); }
        public Vect2f compose(java.lang.Float... s) { return Vect2f.compose(s); }

        public int DIMENSION_COUNT(){return 2;}


        public java.lang.Float getNth(Vect2f v, int n) { return v.getNth(n); }
        public Vect2f withNth(Vect2f p, int n, java.lang.Float s) { return n==0?make(s, p.y):make(p.x, s); }


        public Vect2f ZERO(){return ZERO;}


        public Vect2f MAX_VAL(){return INF;}


        @Override public Class<Vect2f> getVectClass() { return Vect2f.class; }
    }

    /**
     * Transformační funkce, která k oběma složkám vektoru přičítá stejnou konstantní hodnotu.
     * Immutable.
     *
     * @see com.markussecundus.forms.gfx.BinaryGraphicalComposite
     *
     * @author MarkusSecundus
     * */
    public static class FloatAdder implements Function<Vect2f, Vect2f>{
        public FloatAdder(float ammount){this.ammount = ammount;}

        /**
         * Provede transformaci.
         *
         * @return vektor se všemi složkami zvýšenými o <code>ammount</code>
         * */
        public Vect2f apply(Vect2f v) { return v.add(ammount, ammount); }

        /**
         * Hodnota přičítaná k vektoru.
         * */
        public final float ammount;

        /**
         * @return instance s danou hodnotou <code>ammount</code>
         * */
        public static FloatAdder make(float ammount){return new FloatAdder(ammount);}

        /**
         * @return instance s přičítanou hodnotou zvýšenou o parametr funkce
         * */
        public FloatAdder add(float ammount){return new FloatAdder(this.ammount + ammount);}
    }
    /**
     * Transformační funkce, která obě složky vektoru násobí stejnou konstantní hodnotou.
     * Immutable.
     *
     * @see com.markussecundus.forms.gfx.BinaryGraphicalComposite
     *
     * @author MarkusSecundus
     * */
    public static class FloatMultiplier implements Function<Vect2f, Vect2f>{
        public FloatMultiplier(float ammount){this.ammount = ammount;}

        /**
         * Provede transformaci.
         *
         * @return vektor vynásobený hodnotou <code>ammount</code>
         * */
        public Vect2f apply(Vect2f v) { return v.scl(ammount); }

        /**
         * Hodnota, kterou je vektor násoben.
         * */
        public final float ammount;

        /**
         * @return instance s danou hodnotou <code>ammount</code>
         * */
        public static FloatMultiplier make(float ammount){return new FloatMultiplier(ammount);}

        /**
         * @return instance s násobící hodnotou zvýšenou o parametr funkce
         * */
        public FloatMultiplier add(float ammount){return new FloatMultiplier(this.ammount + ammount);}
    }
    /**
     * Transformační funkce, která k vektoru přičítá konstantní hodnotu.
     * Immutable.
     *
     * @see com.markussecundus.forms.gfx.BinaryGraphicalComposite
     *
     * @author MarkusSecundus
     * */
    public static class Adder implements Function<Vect2f, Vect2f>{
        public Adder(Vect2f ammount){this.ammount = ammount;}

        /**
         * Provede transformaci.
         *
         * @return součet vstupní hodnoty s hodnotou <code>ammount</code>
         * */
        public Vect2f apply(Vect2f v) { return v.add(ammount); }
        public final Vect2f ammount;

        /**
         * @return instance s danou hodnotou <code>ammount</code>
         * */
        public static Adder make(Vect2f ammount){return new Adder(ammount);}
        /**
         * @return instance s přičítanou hodnotou zvýšenou o parametr funkce
         * */
        public Adder add(Vect2f ammount){return new Adder(this.ammount.add(ammount));}
        /**
         * @return instance s přičítanou hodnotou zvýšenou o parametr funkce
         * */
        public Adder add(float x, float y){return new Adder(this.ammount.add(x, y));}
    }


    /**
     * Transformační funkce, která k vektoru přičítá hodnotu získanou jako podíl z jeho velikosti.
     * Immutable.
     *
     * @see com.markussecundus.forms.gfx.BinaryGraphicalComposite
     *
     * @author MarkusSecundus
     * */
    public static class ExtractMultAdder implements Function<Vect2f, Vect2f>{

        /**
         * @param ratio poměr, kterým bude pronásobena výsledná hodnota
         * @param extractor funkce, která z vektoru vytáhne číselnou hodnotu, jenž bude následně násobena poměrem
         * */
        public ExtractMultAdder(float ratio, Function<Vect2f, java.lang.Float> extractor){
            this.ratio = ratio;
            this.extractor = extractor;
        }

        /**
         * Provede transformaci.
         *
         * @return přetransformuje vstupní argument skrze <code>extractor</code> a vynásobí ho <code>ratio</code>.
         * */
        public Vect2f apply(Vect2f v){
            float toBeAdded = extractor.apply(v)*ratio;
            return v.add(toBeAdded, toBeAdded);
        }

        /**
         * Funkce, která z vektoru vytáhuje číselnou hodnotu, k násobení poměrem.
         */
        public final Function<Vect2f, java.lang.Float> extractor;
        /**
         * Poměr, k pronásobení výsledné hodnoty.
         * */
        public final float ratio;

        /**
         * @return instance s danou hodnotou <code>ammount</code>
         * */
        public static ExtractMultAdder make(float ratio, Function<Vect2f, java.lang.Float> extractor){return new ExtractMultAdder(ratio, extractor);}

        /**
         * @return instance s danou hodnotou <code>ratio</code> a zachovanou transformační funkcí
         * */
        public ExtractMultAdder with(float ratio){return make(ratio, extractor);}
        /**
         * @return instance s danou transformační funkcí a zachovanou hodnotou <code>ratio</code>
         * */
        public ExtractMultAdder with(Function<Vect2f, java.lang.Float> extractor){return make(ratio, extractor);}
    }

    /**
     * Factory na {@link FloatAdder}.
     *
     * @return instance {@link FloatAdder} s danou hodnotou
     * */
    public static FloatAdder makeAdder(float ammount){return FloatAdder.make(ammount);}

    /**
     * Factory na {@link Adder}.
     *
     * @return instance {@link Adder} s danou hodnotou
     * */
    public static Adder makeAdder(Vect2f ammount){return Adder.make(ammount);}

    /**
     * Factory na {@link FloatMultiplier}.
     *
     * @return instance {@link FloatMultiplier} s danou hodnotou
     * */
    public static FloatMultiplier makeMultiplier(float ammount){return FloatMultiplier.make(ammount);}

    /**
     * Factory na {@link ExtractMultAdder}.
     *
     * @return instance {@link ExtractMultAdder} s danou hodnotou
     * */
    public static ExtractMultAdder makeRatioAdder(float ratio, Function<Vect2f, java.lang.Float> extractor){return ExtractMultAdder.make(ratio, extractor);}


    /**
     * Vytáhne z vektoru jeho minimum.
     *
     * Může sloužit jako parametr pro {@link ExtractMultAdder}.
     * */
    public static final Function<Vect2f, java.lang.Float> minFunc = Vect2f::min;

    /**
     * Vytáhne z vektoru jeho maximum.
     *
     * Může sloužit jako parametr pro {@link ExtractMultAdder}.
     * */
    public static final Function<Vect2f, java.lang.Float> maxFunc = Vect2f::max;

    /**
     * Vytáhne z vektoru průměr jeho složek.
     *
     * Může sloužit jako parametr pro {@link ExtractMultAdder}.
     * */
    public static final Function<Vect2f, java.lang.Float> avgFunc = Vect2f::avg;
}
