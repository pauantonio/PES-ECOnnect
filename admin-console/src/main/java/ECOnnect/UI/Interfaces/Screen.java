package ECOnnect.UI.Interfaces;

import javax.swing.JPanel;

public abstract class Screen {
    
    private final Controller _controller;
    private View _view = null;
    
    protected Screen(Controller controller) {
        this._controller = controller;
    }
    
    public JPanel getPanel() {
        if (_view == null) {
            _view = _controller.getView();
        }
        return _view.getPanel();
    }
    
    // Called after the screen has been initialized and painted
    public void postInit(Object[] args) {
        _controller.postInit(args);
    }
    
    public abstract String getTitle();
}
