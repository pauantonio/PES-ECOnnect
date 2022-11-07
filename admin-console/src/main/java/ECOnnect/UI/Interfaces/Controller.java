package ECOnnect.UI.Interfaces;

public abstract class Controller {
    public abstract View getView();
    
    // Called after the view has been initialized and painted
    public void postInit(Object[] args) {
        // Do nothing by default
    }
}
