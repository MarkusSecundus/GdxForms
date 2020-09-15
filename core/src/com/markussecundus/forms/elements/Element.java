package com.markussecundus.forms.elements;


/**
 * The bare minimal Element of the Form interface.
 *
 * Can only be updated, additional functionality is required for it to be drawn on the screen - see {@link DrawableElem}
 * As is can be used to contain pieces of UI logic, that are to be performed on every update.
 *
 * @author MarkusSecundus
 * */
@FunctionalInterface
public interface Element {
    /**
     * Called each frame of the game loop.
     *
     * @param delta the time in milliseconds elapsed from the last call of <code>update</code>
     * @param frameId the identifier of the current frame - can be used to detect skipped frames or update being called multiple times in one frame
     *                - incremented by 1 on each frame
     *                - does not uniquely define the frame, asi the number of frames in one game run is unlimited, unlike the capacity of <code>int</code> type
     * */
    public void update(float delta, int frameId);
}
