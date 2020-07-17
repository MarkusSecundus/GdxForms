package com.markussecundus.forms.elements.impl.layouts;

import com.markussecundus.forms.elements.Drawable;
import com.markussecundus.forms.elements.UberDrawable;
import com.markussecundus.forms.utils.datastruct.readonly.ReadonlyList_int;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.property.Property;

public class BasicGridLayout<Rend, Pos, Scalar extends Comparable<Scalar>> extends BasicAbstractLayout<Rend, Pos, Scalar>{

    public BasicGridLayout(Pos maxSize, Pos minSize, Pos prefSize, VectUtil<Pos, Scalar> posUtil) {
        super(maxSize, minSize, prefSize, posUtil);
    }



    public static interface Interface<Rend, Pos, Scalar extends Comparable<Scalar>> extends UberDrawable<Rend, Pos>{

        public Property<Pos> defaultCellSize();

        public Property<Scalar> collumnSize(int dim, int collumn);

        public Property<ReadonlyList_int> childCell(Drawable<Rend, Pos> drw);
    }

}
