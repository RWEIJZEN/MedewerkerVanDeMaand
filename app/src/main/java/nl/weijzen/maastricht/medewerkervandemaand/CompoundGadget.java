package nl.weijzen.maastricht.medewerkervandemaand;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class CompoundGadget {
    private static Long numberOfCompoundGadgetsCreated = 0L;

    private Context context;
    private Matrix matrix;
    private int gadgetResourceId;
    private Long gadgetId;
    private int layer;
    private String name;
    private String msg ="message: ";

    // Constructors
    public CompoundGadget(Context context, int gadgetResourceId, String name, Matrix matrix) {
        ++numberOfCompoundGadgetsCreated;
        this.context = context;
        this.gadgetId = numberOfCompoundGadgetsCreated;
        this.gadgetResourceId = gadgetResourceId;
        this.name = name;
        this.matrix = matrix;
    }

    // Getters
    public Matrix getMatrix() {
        return this.matrix;
    }

    public int getGadgetResourceId() {
        return this.gadgetResourceId;
    }

    public Bitmap getGadgetBitmap() {
        Bitmap bitmap = BitmapFactory.decodeResource(this.context.getResources(), this.gadgetResourceId);
        return bitmap;
    }

    public Integer getLayer() {
        return this.layer;
    }

    public String getName(){
        return this.name;
    }

    // Setters
    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public void setGadgetResourceId(int gadgetResourceId) {
        this.gadgetResourceId = gadgetResourceId;
    }

    public void setLayer(Integer layer) {
        if(0 <= layer){
            this.layer = layer;
        } else {
            throw new InstantiationError("Layer must be greater than 0");
        }
    }

    // Services
    public Integer moveGadgetOneLayerToTop(Integer totalNumberOfGadgets){
        if (totalNumberOfGadgets > this.layer){
            ++this.layer;
        }
        return this.layer;
    }

    public Integer moveGadgetOneLayerToBottom(){
        if (0 < this.layer){
            --this.layer;
        }
        return this.layer;
    }
}