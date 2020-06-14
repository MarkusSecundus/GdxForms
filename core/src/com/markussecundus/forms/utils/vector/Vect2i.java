package com.markussecundus.forms.utils.vector;

import java.io.Serializable;



/**
 * Základní implementace immutable 2D vektoru s celočíselnými hodnotami.
 *
 * @see Vect2f
 * @see VectUtil
 *
 * @author MarkusSecundus
 * */
public class Vect2i implements Cloneable, Serializable {

    /**
     * Souřadnice vektoru.
     * */
    public final int x,y;




    /**
     * @return vektor s požadovanými hodnotami.
     * Nemusí nutně jít o novou instanci.
     * */
    public static Vect2i make(int x, int y){
        return (x>=0 && y>=0 && x<POOL_X && y<POOL_Y)? pool[x][y]: new Vect2i(x,y);
    }
    /**
     * @return vektor s požadovanými hodnotami.
     * Nemusí nutně jít o novou instanci.
     * */
    public static Vect2i make(Vect2i v){return v;}
    /**
     * @return vektor s hodnotami odpovídajícími prvním dvěma prvkům v poli.
     * Nemusí nutně jít o novou instanci.
     * */
    public static Vect2i make(Integer[] arr){return make(arr[0], arr[1]);}
    /**
     * @return vektor s požadovanými hodnotami převedenými na typ <code>int</code>.
     * Nemusí nutně jít o novou instanci.
     * */
    public static Vect2i make(Vect2f v){return make((int)v.x, (int)v.y);}

    private Vect2i(int x, int y){this.x = x;this.y = y;}

    private static final int POOL_X = 8, POOL_Y = 32;
    private static final Vect2i[][] pool = new Vect2i[POOL_X][POOL_Y];
    static{
        for(int x = POOL_X;--x>=0;)
            for(int y = POOL_Y;--y>=0;)
                pool[x][y] = new Vect2i(x,y);
    }



    /**
     * @return sama sebe.
     * */
    public Vect2i cpy(){return make(this);}
    /**
     * @return instanci vektoru s opačnými hodnotami.
     * */
    public Vect2i neg(){return make(-x, -y);}
    /**
     * @return převod tohoto vektoru na floatovou variantu.
     * (viz {@link Vect2f})
     * */
    public Vect2f toFloat(){return Vect2f.make(this);}

    /**
     * @return součet tohoto vektoru s danou hodnotou.
     * */
    public Vect2i add(int x, int y){return make(this.x + x, this.y+y);}
    /**
     * @return součet tohoto vektoru s danou hodnotou.
     * */
    public Vect2i add(Vect2i v){return add(v.x,v.y);}

    /**
     * @return rozdíl tohoto vektoru s danou hodnotou.
     * */
    public Vect2i sub(int x, int y){return make(this.x - x, this.y-y);}
    /**
     * @return rozdíl tohoto vektoru s danou hodnotou.
     * */
    public Vect2i sub(Vect2i v){return sub(v.x, v.y);}

    /**
     * @return násobek daného vektoru.
     * */
    public Vect2i scl(double scl){return make((int)(x* scl), (int)(y* scl));}
    /**
     * @return násobek daného vektoru.
     * */
    public Vect2i scl(Integer scl){return scl(scl);}
    /**
     * @return převrácený násobek daného vektoru.
     * */
    public Vect2i div(double scl){return make((int)(x/ scl), (int)(y/ scl));}
    /**
     * @return převrácený násobek daného vektoru.
     * */
    public Vect2i div(Integer scl){return div(scl);}

    /**
     * Vrátí 2. mocninu délky vektoru.
     * Rychlejší než počítat skutečnou délku a ke vzájemnému porovnávání délek slouží stejně dobře.
     *
     * @return 2. mocina délky vektoru
     * */
    public int len2(){return x*x + y*y;}
    /**
     * @return délka vektoru
     * */
    public double len(){return (int)Math.sqrt(len2());}


    /**
     * Vrátí 2. mocninu vzdálenosti od daného vektoru.
     * Rychlejší než počítat skutečnou délku a ke vzájemnému porovnávání vzdáleností slouží stejně dobře.
     *
     * @return 2. mocina vzdálenosti od vstupního vektoru
     * */
    public int dst2(int x, int y){
        return (x=this.x-x)*x + (y=this.y-y)*y;
    }
    /**
     * Vrátí 2. mocninu vzdálenosti od daného vektoru.
     * Rychlejší než počítat skutečnou délku a ke vzájemnému porovnávání vzdáleností slouží stejně dobře.
     *
     * @return 2. mocina vzdálenosti od vstupního vektoru
     * */
    public Integer dst2(Vect2i v){return dst2(v.x, v.y);}
    /**
     * @return vzdálenost od vstupního vektoru
     * */
    public double dst(int x, int y){return Math.sqrt(dst2(x,y));}
    /**
     * @return vzdálenost od vstupního vektoru
     * */
    public double dst(Vect2i v){return dst(v.x, v.y);}


    /**
     * @return minimum z obou souřadnic
     * */
    public int min(){return Math.min(x,y);}
    /**
     * @return maximum z obou souřadnic
     * */
    public int max(){return Math.max(x,y);}


    /**
     * Vektor odpovídající souřadnicím (0,0)^T
     * */
    public static final Vect2i ZERO = make(0,0);

    /**
     * Vektor nabývající nejvyšších souřadnic, jakých lze s datovým typem <code>int</code> dosáhnout.
     * */
    public static final Vect2i MAX_VALUE = make(Integer.MAX_VALUE, Integer.MAX_VALUE);


    /**
     * {@inheritDoc}
     * */
    @Override
    public int hashCode() {
        return (1+x)*x + y;
    }

    /**
     * @return <code>true</code> pokud jsou obě souřadnice vektoru stejné
     * */
    protected boolean equals(Vect2i v){
        return v==this || (v!=null && v.x == x && v.y == y);
    }
    /**
     * Shoda s liboovlnou instancí {@link Vect2i}, pro kterou <code>equals(Vect2i)</code> vrátí <code>true</code>.
     *
     * {@inheritDoc}
     * */
    @Override public boolean equals(Object obj) {
        return obj instanceof Vect2i && ((Vect2i)obj).equals(this);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return Vect2i.make(this);
        }
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public String toString() {
        return String.format("(%d, %d)^T", x,y);
    }







    /**
     * Rozloží vektor na pole složek.
     * */
    public Integer[] decompose(){return new Integer[]{x,y};}
    /**
     * Složí vektor z pole složek.
     * */
    public static Vect2i compose(Integer[] arr){return make(arr);}

    /**
     * Vrátí instanci příslušné {@link VectUtil}.
     * */
    public static VectUtil<Vect2i, Integer> getUtility(){return Util.INSTANCE;}


    /**
     * Utilita pro typ {@link Vect2i}.
     *
     * @see VectUtil
     *
     * @author MarkusSecundus
     * */
    public static class Util extends VectUtil.BasicImplementations.VectorUtil_IntegerAsScalar<Vect2i> {
        private Util(){}

        /**
         * Jediná existující instance.
         * */
        public static final Util INSTANCE = new Util();

        /**{@inheritDoc}*/
        public Vect2i add(Vect2i a, Vect2i b){return a.add(b);}
        /**{@inheritDoc}*/
        public Vect2i sub(Vect2i a, Vect2i b){return a.sub(b);}

        /**{@inheritDoc}*/
        public Vect2i scl(Vect2i a, Integer b){return a.scl(b);}
        /**{@inheritDoc}*/
        public Vect2i div(Vect2i a, Integer b){return a.div(b);}

        /**{@inheritDoc}*/
        public Vect2i scl(Vect2i a, double b) { return a.scl(b); }
        /**{@inheritDoc}*/
        public Vect2i div(Vect2i a, double b) { return a.div(b); }

        /**{@inheritDoc}*/
        public  Integer len2(Vect2i v){return v.len2();}
        /**{@inheritDoc}*/
        public  Integer len(Vect2i v){return (int)v.len();}

        /**{@inheritDoc}*/
        public  Integer dst2(Vect2i a, Vect2i b){return a.dst2(b);}
        /**{@inheritDoc}*/
        public  Integer dst(Vect2i a, Vect2i b){return (int)a.dst(b);}

        /**{@inheritDoc}*/
        public Vect2i cpy(Vect2i v){return v;}

        /**{@inheritDoc}*/
        public Integer minScalar(Vect2i v) { return v.min(); }
        /**{@inheritDoc}*/
        public Integer maxScalar(Vect2i v) { return v.max(); }


        /**{@inheritDoc}*/
        public Integer[] decompose(Vect2i p) { return p.decompose(); }
        /**{@inheritDoc}*/
        public Vect2i compose(Integer[] s) { return Vect2i.compose(s); }
        /**{@inheritDoc}*/
        public int DIMENSION_COUNT(){return 2;}

        /**{@inheritDoc}*/
        public Integer getNth(Vect2i v, int n) { return n==0? v.x: v.y; }
        /**{@inheritDoc}*/
        public Vect2i withNth(Vect2i p, int n, Integer s) { return n==0?make(s, p.y):make(p.x, s); }

        /**{@inheritDoc}*/
        public Vect2i ZERO(){return ZERO;}

        /**{@inheritDoc}*/
        public Vect2i MAX_VAL(){return MAX_VALUE;}

        /**{@inheritDoc}*/
        @Override public Class<Vect2i> getVectClass() { return Vect2i.class; }
    }

}
