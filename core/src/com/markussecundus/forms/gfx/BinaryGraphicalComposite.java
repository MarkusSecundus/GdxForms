package com.markussecundus.forms.gfx;

import com.markussecundus.forms.events.EventListener;
import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.utils.function.Function;
import com.markussecundus.forms.utils.vector.Vect2f;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty;


/**
 * Generická složenina dvou různých {@link GraphicalPrimitive}.
 *
 * Stará se o vykreslování obou složek a synchronizaci jejich velikostí.
 *
 * @param <Rend> Typ objektu, skrze který bude prováděno vykreslení na obrazovku či kamkoliv jinam
 * @param <Pos> Vektorový typ, v jehož souřadnicích grafika obou složek funguje
 * @param <Scalar> Skalární typ, z něhož sestávají jednotlivé složky vektoru <code>Pos</code>
 *
 * @param <Obj1> Typ první složky - ta bude vykreslována jako 1., přes ní pak bude vykreslena instance <code>Obj2</code>
 * @param <Obj2> Typ druhé složky - ta bude vykreslována jako 2. přes instanci <code>Obj1</code>
 * @param <Ratio> Typ funkce, která udává vztah mezi velikostí 1. a 2. složky
 *
 * @see com.markussecundus.forms.gfx.GraphicalPrimitive
 *
 * @author MarkusSecundus
 * */
public class BinaryGraphicalComposite<Rend, Pos, Scalar extends Comparable<Scalar>, Obj1 extends GraphicalPrimitive<Rend, Pos, Scalar>, Obj2 extends GraphicalPrimitive<Rend, Pos, Scalar>, Ratio extends Function<Pos, Pos>> implements GraphicalPrimitive<Rend, Pos, Scalar> {
//public:
    /**
     * Zkonstruuje slepenec obou objektů a přidá jim listenery
     * pro synchronizaci jejich velikostí.
     *
     * Jako výchoýí hodnotu pro <code>obj2detachment</code> použije <code>obj1.getVectUtil().ZERO()</code>.
     *
     * @param obj1 první složka - bude vykreslována jako 1., přes ní pak bude vykreslena <code>obj2</code>
     * @param obj2 druhá složka - bude vykreslována jako 2., případně překryje <code>obj1</code>
     * @param obj1to2ratio funkce udávající poměr velikosti <code>obj2</code> vůči velikosti <code>obj1</code>
     * */
    public BinaryGraphicalComposite(Obj1 obj1, Obj2 obj2, Ratio obj1to2ratio){
        this(obj1, obj2, obj1.getVectUtil().ZERO(), obj1to2ratio);
    }

    /**
     * Zkonstruuje slepenec obou objektů a přidá jim listenery
     * pro synchronizaci jejich velikostí.
     *
     * @param obj1 první složka - bude vykreslována jako 1., přes ní pak bude vykreslena <code>obj2</code>
     * @param obj2 druhá složka - bude vykreslována jako 2., případně překryje <code>obj1</code>
     * @param obj1to2ratio funkce udávající poměr velikosti <code>obj2</code> vůči velikosti <code>obj1</code>
     * @param obj2detachment výchozí hodnota pro Property <code>obj2detachment()</code> - udává
     *                      odstup vykreslovací báze <code>obj2</code> od vykreslovací báze <code>obj1</code>
     * */
    public BinaryGraphicalComposite(Obj1 obj1, Obj2 obj2, Pos obj2detachment, Ratio obj1to2ratio){
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.obj2detachment = new SimpleProperty<>(obj2detachment);
        this.obj1to2ratio = new SimpleProperty<>(obj1to2ratio);


        this.obj1to2ratio().getSetterListeners()._getPostUtilListeners().add(e->{
            this.obj2.setDimensions(e.newVal().get().apply(obj1.getDimensions()));
            return true;
        });
        this.dimensions().getSetterListeners()._getPostUtilListeners().add(e->{
            this.obj2.setDimensions(getObj1to2ratio().apply(e.newVal().get()));
            return true;
        });
        this.obj1to2ratio().pretendSet();
    }

    /**
     * Vykreslí obě komponenty na obrazovku.
     * Nejprve <code>obj1</code>, poté <code>obj2</code>,
     * <code>obj1</code> na pozici dané parametrem <code>pos</code>,
     * <code>obj2</code> pak posunutou o hodnotu <code>obj2detachment</code>.
     *
     * @param pos pozice na obrazovce, na které má objekt být vykreslen,
     *            dle konvence udává levý dolní roh obdélníku opsaného
     *            tomuto objektu (příp. zobecnění této definice pro n-rozměrný prostor)
     * @param renderer objekt, který provede samotné vykreslení na obrazovkuss
     *
     * @return <code>true</code> pokud alespon 1 z komponent vrátila <code>true</code>,
     *  jinak <code>false</code>
     * */
    @Override public boolean draw(Rend renderer, Pos pos) {
        return obj1.draw(renderer, pos) | obj2.draw(renderer, POS().add(POS().cpy(pos), getObj2detachment()));
    }

    /**
     * Odkazuje na <code>obj1.dimension()</code>.
     *
     * {@inheritDoc}
     * */
    @Override public Property<Pos> dimensions() {
        return obj1.dimensions();
    }

    /**
     * Vzdálenost, o kterou je posunuta báze vykreslování <code>obj2</code>
     * oproti základní bázi vykreslování, s kterou je volána funkce <code>draw</code>.
     * */
    public Property<Pos> obj2detachment(){ return obj2detachment; }

    /**
     * Pohodlnější zkratka pro '<code>obj2detachment().get()</code>'
     * */
    public Pos getObj2detachment(){ return obj2detachment().get(); }

    /**
     * Pohodlnější zkratka pro '<code>obj2detachment().set(newDetachment)</code>'
     * */
    public Pos setObj2detachment(Pos newDetachment){ return obj2detachment().set(newDetachment); }

    /**
     * Funkce, udávající poměr rozměrů <code>obj2</code> oproti rozměrům <code>obj1</code>,
     * resp. rozměrům celého {@link BinaryGraphicalComposite}.
     * */
    public Property<Ratio> obj1to2ratio(){return obj1to2ratio;}

    /**
     * Pohodlnější zkratka pro '<code>obj1to2ratio().get()</code>'
     * */
    public Ratio getObj1to2ratio(){return obj1to2ratio().get();}

    /**
     * Pohodlnější zkratka pro '<code>obj1to2ratio().set(newRatio)</code>'
     *
     * @param newRatio nový poměr pro velikost <code>obj2</code>.
     * */
    public Ratio setObj1to2ratio( Ratio newRatio){return obj1to2ratio().set(newRatio);}


    /**
     * {@inheritDoc}
     * */
    @Override public VectUtil<Pos, Scalar> getVectUtil() { return obj1.getVectUtil(); }

    /**
     * 1. komponenta slepence
     * */
    public final Obj1 obj1;

    /**
     * 2. komponenta slepence
     * */
    public final Obj2 obj2;


    /**
     * Přidá listener zaručující, že <code>obj2</code> se bude vždy nalézat na poloze vycentrované
     * vůči celkové velikosti s příslušnými poměry.
     *
     * Centruje defaultně na pravý střed (všechny poměry == 0.5d).
     *
     * @return {@link EventListener} který byl přidán do <code>obj2.dimension().getSetterListeners()._getPostUtilListeners()</code>
     * */
    public EventListener<Property.SetterListenerArgs<Pos>> setCentered(){
        return setCentered(FormsUtil.fillArray(new double[POS().DIMENSION_COUNT()], 0.5d));
    }

    /**
     * Přidá listener zaručující, že <code>obj2</code> se bude vždy nalézat na poloze vycentrované
     * vůči celkové velikosti s příslušnými poměry.
     *
     * @param ratios pole délky <code>getVectUtil().DIMENSIO_COUNT()</code>, hodnot v intervalu [0d;1d],
     *               udávajících poměr, na který má být v dané dimenzi vnitřní komponenta
     *               vycentrována
     *
     * @return {@link EventListener} který byl přidán do <code>obj2.dimension().getSetterListeners()._getPostUtilListeners()</code>
     * */
    public EventListener<Property.SetterListenerArgs<Pos>> setCentered(double[] ratios){
        EventListener<Property.SetterListenerArgs<Pos>> list = e->{
            Pos dim1 = POS().cpy(getDimensions());
            Pos dim2 = e==null?obj2.getDimensions():e.newVal().get();
            dim2 = POS().sub(dim1, dim2);
            dim2 = POS().sclComponents(dim2, ratios);
            setObj2detachment(dim2);
            return true;
        };
        obj2.dimensions().getSetterListeners()._getPostUtilListeners().add(list);
        obj2.dimensions().pretendSet();
        return list;
    }

    /**
     * Vytvoří novou instanci {@link BinaryGraphicalComposite} s vnitřní komponentou rovnou vystředěnou na pravý střed.
     *
     *
     * @param o1 první složka - bude vykreslována jako 1., přes ní pak bude vykreslena <code>obj2</code>
     * @param o2 druhá složka - bude vykreslována jako 2., případně překryje <code>obj1</code>
     * @param obj1to2ratio funkce udávající poměr velikosti <code>obj2</code> vůči velikosti <code>obj1</code>
     * */
    public static <Rend, Pos, Scalar extends Comparable<Scalar>, Obj1 extends GraphicalPrimitive<Rend, Pos, Scalar>, Obj2 extends GraphicalPrimitive<Rend, Pos, Scalar>, Ratio extends Function<Pos, Pos>>
    BinaryGraphicalComposite<Rend, Pos, Scalar, Obj1, Obj2, Ratio> makeCentered(Obj1 o1, Obj2 o2, Ratio obj1to2ratio){
        BinaryGraphicalComposite<Rend, Pos, Scalar, Obj1, Obj2, Ratio> ret = new BinaryGraphicalComposite<>(o1, o2, obj1to2ratio);
        ret.setCentered();
        ret.obj2.dimensions().pretendSet();
        return ret;
    }

//private:
    private final Property<Pos> obj2detachment;

    private final Property<Ratio> obj1to2ratio;

    /**
     * Kratší a pohodlnější alias pro <code>getVectUtil</code>.
     * */
    private VectUtil<Pos, Scalar> POS(){return getVectUtil();}





//public:

    /**
     * Kratší a na použití pohodlnější alias pro {@link BinaryGraphicalComposite} počítající s {@link Vect2f},
     * který je vystředěný a poměr velikostí udává funkce typu <code>Vect2f.ExtractMultAdder</code>.
     *
     * Základ pro vytváření grafických objektů, kde vnější objekt slouží jako obruba
     * objektu vnějšího a tloušťka obruby je relativní vůči celkové velikosti objektu.
     *
     * @param <Rend> Typ objektu, skrze který bude prováděno vykreslení na obrazovku či kamkoliv jinam
     * @param <Drw> typ, ke kterěmu náleží obě komponenty
     *
     * @see com.markussecundus.forms.gfx.BinaryGraphicalComposite
     * @see Vect2f
     *
     * @author MarkusSecundus
     * */
    public static class RelativniObruba<Rend, Drw extends GraphicalPrimitive<Rend, Vect2f, Float>> extends BinaryGraphicalComposite<Rend, Vect2f, Float, Drw, Drw, Vect2f.ExtractMultAdder> {

        /**
         * Vytvoří kompositum vycentrované na pravý střed, s obrubou relativní vůči své velikosti.
         *
         * @param borderRatio poměr tloušťky obruby vůči celkové velikosti.
         * @param inner vnitřní grafický útvar, který bude vykreslen celý
         * @param outer obrubový grafický útvar, přes který bude vykreslen útvar<code>inner</code>
         * */
        public RelativniObruba(float borderRatio, Drw outer, Drw inner){
            super(inner, outer, Vect2f.makeRatioAdder(-borderRatio, Vect2f.minFunc));
            setCentered();
        }

        /**
         * Vrátí vnitřní grafickou komponentu.
         * @return vnitřní grafická komponenta
         * */
        public Drw getInner(){return obj2;}

        /**
         * Vrátí vnější (obrubovou) grafickou komponentu.
         * @return vnější (obrubová) grafická komponenta
         * */
        public Drw getOuter(){return obj1;}
    }

    /**
     * Kratší a na použití pohodlnější alias pro {@link BinaryGraphicalComposite} počítající s {@link Vect2f},
     * který je vystředěný a poměr velikostí udává funkce typu <code>Vect2f.Adder</code>.
     *
     * Základ pro vytváření grafických objektů, kde vnější objekt slouží jako obruba
     * objektu vnějšího a tloušťka obruby je konstantní.
     *
     * @param <Rend> Typ objektu, skrze který bude prováděno vykreslení na obrazovku či kamkoliv jinam
     * @param <Drw> typ, ke kterěmu náleží obě komponenty
     *
     * @see com.markussecundus.forms.gfx.BinaryGraphicalComposite
     * @see Vect2f
     *
     * @author MarkusSecundus
     * */
    public static class Obruba<Rend, Drw extends GraphicalPrimitive<Rend, Vect2f, Float>> extends BinaryGraphicalComposite<Rend, Vect2f, Float, Drw, Drw,  Vect2f.Adder>{

        /**
         * Vytvoří kompositum vycentrované na pravý střed, s obrubou konstantních rozměrů.
         *
         * @param borderSize rozměry obruby
         * @param inner vnitřní grafický útvar, který bude vykreslen celý
         * @param outer obrubový grafický útvar, přes který bude vykreslen útvar<code>inner</code>
         * */
        public Obruba(Vect2f borderSize, Drw outer, Drw inner){
            super(outer, inner, Vect2f.makeAdder(borderSize.neg()));
            setCentered();
        }

        /**
         * Vrátí vnitřní grafickou komponentu.
         * @return vnitřní grafická komponenta
         * */
        public Drw getInner(){return obj2;}

        /**
         * Vrátí vnější (obrubovou) grafickou komponentu.
         * @return vnější (obrubová) grafická komponenta
         * */
        public Drw getOuter(){return obj1;}
    }
}
