package com.markussecundus.forms.elements.impl;

import com.markussecundus.forms.elements.impl.utils.DefaultSizeBehavior;
import com.markussecundus.forms.events.EventListenerX;
import com.markussecundus.forms.text.Font;
import com.markussecundus.forms.utils.datastruct.ReadonlyList;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty;


/**
 *
 * */
public class BasicLabel<Rend, Pos, Scalar extends Comparable<Scalar>> extends BasicAbstractDrawableElem<Rend, Pos, Scalar> {

    /**
     * Inits the Label with default values.
     * <p></p>
     * maxSize:  vectUtil.MAX_VAL() <p>
     * minSize:  vectUtil.ZERO() <p>
     * prefSize: vectUtil.ZERO()
     * <p></p>
     * allignment: [0d,... ,0d]
     * <p></p>
     * font: null <p>
     * text: ""
     *
     * @param vectUtil <code>Pos</code> decomposer to initilize the {@link DefaultSizeBehavior} component
     */
    public BasicLabel(VectUtil<Pos, Scalar> vectUtil){
        this(vectUtil, vectUtil.ZERO(), null, "");
    }

    /**
     * Inits the Label with default values.
     * <p></p>
     * maxSize:  vectUtil.MAX_VAL() <p>
     * minSize:  vectUtil.ZERO() <p>
     * allignment: [0d,... ,0d]
     *
     * @param vectUtil <code>Pos</code> decomposer to initilize the {@link DefaultSizeBehavior} component
     * @param prefSize   default value for <code>prefSize</code> Property
     * @param font   default value for <code>font</code> Property
     * @param text   default value for <code>text</code> Property
     */
    public BasicLabel(VectUtil<Pos, Scalar> vectUtil, Pos prefSize, Font<Rend, Pos, Scalar> font, String text){
        this(vectUtil, vectUtil.MAX_VAL(), vectUtil.ZERO(), prefSize, font, ReadonlyList.ofItem(0d, vectUtil.DIMENSION_COUNT()), text);
    }


    /**
     * Inits the Label.
     *
     * @param vectUtil <code>Pos</code> decomposer to initilize the {@link DefaultSizeBehavior} component
     * @param maxSize    default value for <code>maxSize</code> Property
     * @param minSize    default value for <code>minSize</code> Property
     * @param prefSize   default value for <code>prefSize</code> Property
     * @param font   default value for <code>font</code> Property
     * @param allignment   default value for <code>allignment</code> Property
     * @param text   default value for <code>text</code> Property
     */
    public BasicLabel(VectUtil<Pos, Scalar> vectUtil, Pos maxSize, Pos minSize, Pos prefSize, Font<Rend, Pos, Scalar> font, ReadonlyList.Double allignment, String text) {
        super(vectUtil, maxSize, minSize, prefSize);
        this.POS = vectUtil;

        this.font = new SimpleProperty<>(font);
        this.allignment = new SimpleProperty<>(allignment);
        this.text = new SimpleProperty<>(text);

        EventListenerX<Object> dirtySetter = o-> markDirty();
        this.font().getSetterListeners()._getUtilListeners().add(dirtySetter);
        this.allignment().getSetterListeners()._getUtilListeners().add(dirtySetter);
        this.text().getSetterListeners()._getUtilListeners().add(dirtySetter);
        this.size().getSetterListeners()._getUtilListeners().add(dirtySetter);
    }



    /**
     * @return The instance of {@link Font} used to draw text.
     * */
    public Property<Font<Rend, Pos, Scalar>> font(){return font;}
    /**
     * @return Allignment of the text.
     * */
    public Property<ReadonlyList.Double> allignment(){return allignment;}

    /**
     * @return The text of the label.
     * */
    public Property<String> text(){return text;}



    /**
     * @return shinier shortcut for <code>font.get()</code>
     * */
    public final Font<Rend, Pos, Scalar> getFont(){return font().get();}
    /**
     * @return shinier shortcut for <code>allignment.get()</code>
     * */
    public final ReadonlyList.Double getAllignment(){return allignment().get();}
    /**
     * @return shinier shortcut for <code>text.get()</code>
     * */
    public final String getText(){return text().get();}

    /**
     * @return shinier shortcut for <code>font().set(newVal)</code>
     * */
    public final Font<Rend, Pos, Scalar> setFont(Font<Rend, Pos, Scalar> newVal){return font().set(newVal);}
    /**
     * @return shinier shortcut for <code>allignment().set(newVal)</code>
     * */
    public final ReadonlyList.Double setAllignment(ReadonlyList.Double newVal){return allignment().set(newVal);}
    /**
     * @return shinier shortcut for <code>text().set(newVal)</code>
     * */
    public final String setText(String newVal){return text().set(newVal);}




    @Override public boolean draw(Rend renderer, Pos position) {
        if(drawingInstruction == null)
            drawingInstruction = computeDrawingInstructions();

        drawingInstruction.font.draw(renderer, POS.add(POS.cpy(position), drawingInstruction.relativePos), drawingInstruction.text);
        return true;
    }




    @Override public VectUtil<Pos, Scalar> getVectUtil() { return POS; }

    @Override public void update(float delta, int frameId) { }

    /**
     * Marks that the rendering metadata need to be recomputed.
     * */
    public void markDirty(){this.drawingInstruction = null;}

//private:
    private final VectUtil<Pos, Scalar> POS;

    private final Property<Font<Rend, Pos, Scalar>> font;
    private final Property<ReadonlyList.Double> allignment;
    private final Property<String> text;


    private static class DrawingInstruction<Rend, Pos, Scalar extends Comparable<Scalar>>{
        public final Font<Rend, Pos, Scalar> font;
        public final Pos relativePos;
        public final String text;

        public DrawingInstruction(Font<Rend, Pos, Scalar> font, Pos allignedPosition, String text) {
            this.font = font;
            this.relativePos = allignedPosition;
            this.text = text;
        }
    }

    /**null if dirty*/
    private DrawingInstruction<Rend, Pos, Scalar> drawingInstruction = null;


    //TODO: ošetřit když je velikost textu větší než velikost labelu
    private DrawingInstruction<Rend, Pos, Scalar> computeDrawingInstructions(){
        Font<Rend, Pos, Scalar> font = getFont();
        String text = getText();
        ReadonlyList.Double allignment = getAllignment();

        Pos size = POS.cpy(this.getSize());
        Pos textDims = font.computeSize(text);

        Pos leftForAllignment = POS.sub(size, textDims);

        if(!POS.ge(leftForAllignment, POS.ZERO())){
            double[] fontScales = font.getScale().toArray();
            for(int dim = fontScales.length ; --dim>=0 ;){
                if(POS.getNth(leftForAllignment, dim).compareTo(POS.ZERO_SCALAR()) < 0){
                    fontScales[dim] *= POS.ratioScalar(POS.getNth(size, dim), POS.getNth(textDims, dim));
                }
            }
            font = font.withScale(fontScales);

            textDims = font.computeSize(text);
            leftForAllignment = POS.sub(size, textDims);
        }


        Pos allignedPosition = POS.sclComponents(leftForAllignment, allignment);

        return new DrawingInstruction<>(font, allignedPosition, text);
    }
}
