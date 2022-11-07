package ECOnnect.UI.ProductTypes.Questions;

import ECOnnect.API.ProductTypesService;
import ECOnnect.API.ServiceFactory;

public class QuestionItemModel {
    
    void editQuestion(int id, String newStatement) {
        ProductTypesService service = ServiceFactory.getInstance().getProductTypesService();
        service.editQuestion(id, newStatement);
    }
    
    void deleteQuestion(int id) {
        ProductTypesService service = ServiceFactory.getInstance().getProductTypesService();
        service.deleteQuestion(id);
    }
}
