package com.markussecundus.formsgdx.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.markussecundus.forms.elements.DrawableElem;

/**
 * Rozhraní, určené jako <code>Rend</code> parametr pro {@link DrawableElem}s,
 * která ke svému vykreslení potřebují pouze služby {@link Batch}.
 *
 * @author MarkusSecundus
 * @see BasicRenderer
 * @see BasicShapeRenderer
 * @see Batch
 * @see com.badlogic.gdx.graphics.g2d.SpriteBatch
 */
public interface BatchRenderer extends Batch {

    /**
     * Nastartuje a vrátí instanci {@link Batch}.
     *
     * @return Instance {@link Batch} připravená kreslit na obrazovku
     */
    public Batch getBatch();


    @Override
    public default void begin() {
        getBatch().begin();
    }

    @Override
    public default void end() {
        getBatch().end();
    }

    @Override
    public default void setColor(Color tint) {
        getBatch().setColor(tint);
    }

    @Override
    public default void setColor(float r, float g, float b, float a) {
        getBatch().setColor(r, g, b, a);
    }

    @Override
    public default Color getColor() {
        return getBatch().getColor();
    }

    @Override
    public default void setPackedColor(float packedColor) {
        getBatch().setPackedColor(packedColor);
    }

    @Override
    public default float getPackedColor() {
        return getBatch().getPackedColor();
    }

    @Override
    public default void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
        getBatch().draw(texture, x, y, originX, originY, width, height, scaleX, scaleY, rotation, srcX, srcY, srcWidth, srcHeight, flipX, flipY);
    }

    @Override
    public default void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {
        getBatch().draw(texture, x, y, width, height, srcX, srcY, srcWidth, srcHeight, flipX, flipY);
    }

    @Override
    public default void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
        getBatch().draw(texture, x, y, srcX, srcY, srcWidth, srcHeight);
    }

    @Override
    public default void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {
        getBatch().draw(texture, x, y, width, height, u, v, u2, v2);
    }

    @Override
    public default void draw(Texture texture, float x, float y) {
        getBatch().draw(texture, x, y);
    }

    @Override
    public default void draw(Texture texture, float x, float y, float width, float height) {
        getBatch().draw(texture, x, y, width, height);
    }

    @Override
    public default void draw(Texture texture, float[] spriteVertices, int offset, int count) {
        getBatch().draw(texture, spriteVertices, offset, count);
    }

    @Override
    public default void draw(TextureRegion region, float x, float y) {
        getBatch().draw(region, x, y);
    }

    @Override
    public default void draw(TextureRegion region, float x, float y, float width, float height) {
        getBatch().draw(region, x, y, width, height);
    }

    @Override
    public default void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
        getBatch().draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
    }

    @Override
    public default void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) {

        getBatch().draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation, clockwise);
    }

    @Override
    public default void draw(TextureRegion region, float width, float height, Affine2 transform) {
        getBatch().draw(region, width, height, transform);
    }

    @Override
    public default void flush() {
        getBatch().flush();
    }

    @Override
    public default void disableBlending() {
        getBatch().disableBlending();
    }

    @Override
    public default void enableBlending() {
        getBatch().enableBlending();
    }

    @Override
    public default void setBlendFunction(int srcFunc, int dstFunc) {
        getBatch().setBlendFunction(srcFunc, dstFunc);
    }

    @Override
    public default void setBlendFunctionSeparate(int srcFuncColor, int dstFuncColor, int srcFuncAlpha, int dstFuncAlpha) {
        getBatch().setBlendFunctionSeparate(srcFuncColor, dstFuncColor, srcFuncAlpha, dstFuncAlpha);
    }

    @Override
    public default int getBlendSrcFunc() {
        return getBatch().getBlendSrcFunc();
    }

    @Override
    public default int getBlendDstFunc() {
        return getBatch().getBlendDstFunc();
    }

    @Override
    public default int getBlendSrcFuncAlpha() {
        return getBatch().getBlendSrcFuncAlpha();
    }

    @Override
    public default int getBlendDstFuncAlpha() {
        return getBatch().getBlendDstFuncAlpha();
    }

    @Override
    public default Matrix4 getProjectionMatrix() {
        return getBatch().getProjectionMatrix();
    }

    @Override
    public default Matrix4 getTransformMatrix() {
        return getBatch().getTransformMatrix();
    }

    @Override
    public default void setProjectionMatrix(Matrix4 projection) {
        getBatch().setProjectionMatrix(projection);
    }

    @Override
    public default void setTransformMatrix(Matrix4 transform) {
        getBatch().setTransformMatrix(transform);
    }

    @Override
    public default void setShader(ShaderProgram shader) {
        getBatch().setShader(shader);
    }

    @Override
    public default ShaderProgram getShader() {
        return getBatch().getShader();
    }

    @Override
    public default boolean isBlendingEnabled() {
        return getBatch().isBlendingEnabled();
    }

    @Override
    public default boolean isDrawing() {
        return getBatch().isDrawing();
    }

    @Override
    public default void dispose() {
        getBatch().dispose();
    }
}
