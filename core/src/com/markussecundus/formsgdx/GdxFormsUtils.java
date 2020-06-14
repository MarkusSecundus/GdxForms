package com.markussecundus.formsgdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.markussecundus.forms.gfx.BinaryGraphicalComposite;
import com.markussecundus.forms.gfx.GraphicalPrimitive;
import com.markussecundus.forms.utils.vector.Vect2f;
import com.markussecundus.forms.utils.vector.Vect2i;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.formsgdx.graphics.RoundedRectangle;

import java.lang.reflect.Field;

public class GdxFormsUtils {

    /**
     * @param v vektor k převedení na nativní typ LibGDX
     *
     * @return instance {@link Vector2} s ekvivalentní hodnotou
     * */
    public static Vector2 convVect(Vect2f v){return new Vector2(v.x, v.y);}
    /**
     * @param v vektor k převedení na nativní typ Formulářové Knihovny
     *
     * @return instance {@link Vect2f} s ekvivalentní hodnotou
     * */
    public static Vect2f convVect(Vector2 v){return Vect2f.make(v.x, v.y);}


    /**
     * Parciální implementace {@link VectUtil} poskytující funkcionalitu společnou
     * pro všechny vektorové typy enginu LibGDX.
     *
     * @see VectUtil
     *
     * @see Vector
     * @see Vector2
     * @see Vector3
     *
     * @author MarkusSecundus
     * */
    public abstract static class GdxVectorUtil<Vect extends Vector<Vect>> extends VectUtil.BasicImplementations.VectorUtil_FloatAsScalar<Vect> {
        /**{@inheritDoc}*/
        @Override
        public Vect add(Vect a, Vect b) { return a.add(b); }

        /**{@inheritDoc}*/
        @Override
        public Vect sub(Vect a, Vect b) { return a.sub(b); }


        /**{@inheritDoc}*/
        @Override
        public Vect scl(Vect a, Float b) { return a.scl(b); }

        /**{@inheritDoc}*/
        @Override
        public Vect div(Vect a, Float b) { return a.scl(1/b); }

        /**{@inheritDoc}*/
        @Override
        public Vect scl(Vect a, double b) { return a.scl((float)b); }

        /**{@inheritDoc}*/
        @Override
        public Vect div(Vect a, double b) { return scl(a,1/b); }


        /**{@inheritDoc}*/
        @Override
        public Float len2(Vect v) { return v.len2(); }

        /**{@inheritDoc}*/
        @Override
        public Float len(Vect v) { return v.len(); }


        /**{@inheritDoc}*/
        @Override
        public Float dst2(Vect a, Vect b) { return a.dst2(b); }

        /**{@inheritDoc}*/
        @Override
        public Float dst(Vect a, Vect b) { return a.dst(b); }


        /**{@inheritDoc}*/
        @Override
        public Vect cpy(Vect a) { return a.cpy(); }
    }

    /**
     * Implementace {@link VectUtil} pro typ {@link Vector2}
     *
     * @see VectUtil
     *
     * @see GdxVectorUtil
     *
     * @see Vector
     * @see Vector2
     *
     * @author MarkusSecundus
     * */
    public static class Vector2Util extends GdxVectorUtil<Vector2>{
        private Vector2Util(){}

        /**
         * Jediná instance.
         * */
        public static final Vector2Util INSTANCE = new Vector2Util();


        /**{@inheritDoc}*/
        @Override
        public Vector2 ZERO() { return Vector2.Zero.cpy(); }

        /**{@inheritDoc}*/
        @Override
        public Vector2 MAX_VAL() { return new Vector2(MAX_VAL_SCALAR(), MAX_VAL_SCALAR()); }

        /**{@inheritDoc}*/
        @Override
        public int DIMENSION_COUNT() { return 2; }


        /**{@inheritDoc}*/
        @Override
        public Float getNth(Vector2 v, int n) { return n==0? v.x: v.y; }

        /**{@inheritDoc}*/
        @Override
        public Vector2 withNth(Vector2 p, int n, Float s) { return n==0?p.cpy().set(s, p.y):p.cpy().set(p.x, s); }


        /**{@inheritDoc}*/
        @Override
        public Float[] decompose(Vector2 p) { return new Float[]{p.x, p.y}; }

        /**{@inheritDoc}*/
        @Override
        public Vector2 compose(Float[] s) { return new Vector2(s[0],s[1]); }


        /**{@inheritDoc}*/
        @Override
        public Float minScalar(Vector2 v) { return Math.min(v.x, v.y); }

        /**{@inheritDoc}*/
        @Override
        public Float maxScalar(Vector2 v) { return Math.max(v.x, v.y); }

        /**{@inheritDoc}*/
        @Override
        public Class<Vector2> getVectClass() { return Vector2.class; }
    }

    /**
     * Implementace {@link VectUtil} pro typ {@link Vector3}
     *
     * @see VectUtil
     *
     * @see GdxVectorUtil
     *
     * @see Vector
     * @see Vector3
     *
     * @author MarkusSecundus
     * */
    public static class Vector3Util extends GdxVectorUtil<Vector3>{
        private Vector3Util(){}

        /**
         * Jediná instance.
         * */
        public static final Vector3Util INSTANCE = new Vector3Util();

        /**{@inheritDoc}*/
        @Override
        public Vector3 ZERO() { return Vector3.Zero.cpy(); }

        /**{@inheritDoc}*/
        @Override
        public Vector3 MAX_VAL() { return new Vector3(MAX_VAL_SCALAR(), MAX_VAL_SCALAR(), MAX_VAL_SCALAR()); }

        /**{@inheritDoc}*/
        @Override
        public int DIMENSION_COUNT() { return 3; }


        /**{@inheritDoc}*/
        @Override
        public Float getNth(Vector3 v, int n) {
            switch (n){
                case 0:return v.x;
                case 1:return v.y;
                case 2:return v.z;
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        /**{@inheritDoc}*/
        @Override
        public Vector3 withNth(Vector3 p, int n, Float s) {
            Vector3 ret = p.cpy();
            switch(n){
                case 0:p.x = s;break;
                case 1:p.y = s;break;
                case 2:p.z = s;break;
                default:
                    throw new IndexOutOfBoundsException();
            }
            return ret;
        }


        /**{@inheritDoc}*/
        @Override
        public Float[] decompose(Vector3 p) { return new Float[]{p.x, p.y, p.z}; }

        /**{@inheritDoc}*/
        @Override
        public Vector3 compose(Float[] s) { return new Vector3(s[0], s[1], s[2]); }


        /**{@inheritDoc}*/
        @Override
        public Class<Vector3> getVectClass() { return Vector3.class; }
    }



}
