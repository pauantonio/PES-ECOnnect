package ECOnnect.UI.ProductTypes.Questions;

import ECOnnect.API.ProductTypesService.ProductType;
import ECOnnect.API.ProductTypesService.ProductType.Question;
import ECOnnect.UI.Interfaces.Controller;
import ECOnnect.UI.Interfaces.View;
import ECOnnect.UI.ProductTypes.ProductTypesModel;

public class ProductTypeQuestionsController extends Controller {
    private final QuestionsView _view = new QuestionsView(() -> newQuestionButton());
    private final ProductTypesModel _model = new ProductTypesModel();
    private String _type; 
    
    @Override
    public View getView() {
        return _view;
    }
    
    @Override
    public void postInit(Object[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 arguments: productType:String, questions:Question[]");
        }
        _type = (String) args[0];
        Question[] questions = (Question[]) args[1];
        
        // Set title
        _view.setTitle("Questions for product type '" + _type + "'");
        
        // Set questions
        _view.setQuestions(questions);
    }
    
    
    // Called when the "New question" button is pressed
    private void newQuestionButton() {
        try {
            // Create new question
            _model.createQuestion(_type);
        }
        catch (Exception e) {
            _view.displayError("Could not create new question: " + e.getMessage());
        }
        
        // Refresh question list
        try {
            ProductType[] types = _model.getProductTypes();
            // Find our product type
            for (ProductType type : types) {
                if (type.name.equals(_type)) {
                    // Update question list
                    _view.setQuestions(type.questions);
                    return;
                }
            }
            throw new IllegalStateException("Could not find product type '" + _type + "'");
        }
        catch (Exception e) {
            _view.displayError("Could not refresh question list: " + e.getMessage());
        }
    }
}
