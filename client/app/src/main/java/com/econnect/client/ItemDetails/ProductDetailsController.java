package com.econnect.client.ItemDetails;

import android.graphics.Bitmap;

import com.econnect.API.ProductService;
import com.econnect.API.ProductService.ProductDetails;
import com.econnect.API.ServiceFactory;
import com.econnect.Utilities.BitmapLoader;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.PopupMessage;
import com.econnect.API.QuestionService;
import com.econnect.API.ReviewService;
import com.econnect.client.R;

public class ProductDetailsController implements IDetailsController {

    private final ProductDetailsFragment _fragment;
    private final int _productId;
    private ProductDetails _product;
    private int stars;

    public ProductDetailsController(ProductDetailsFragment fragment, int productId) {
        this._fragment = fragment;
        this._productId = productId;
        stars = 0;
    }

    public void setStars(int i){
        stars = i;
        _fragment.updateStars(stars);
    }
    @Override
    public void updateUIElements() {
        ExecutionThread.nonUI(() -> {
            try {
                // Get product
                ProductService service = ServiceFactory.getInstance().getProductService();
                _product = service.getProductDetails(_productId);

                ExecutionThread.UI(_fragment, () -> {
                    _fragment.setTitle(_product.name);
                    _fragment.setAverageRating(_product.ratings);
                    _fragment.setQuestionsElements(_product.questions);
                });

                // Fetch image
                Bitmap bmp = BitmapLoader.fromURLResizeHeight(_product.imageURL, 128);
                ExecutionThread.UI(_fragment, () -> {
                    if (bmp != null) _fragment.setImage(bmp);
                });
            }
            catch (Exception e) {
                ExecutionThread.UI(_fragment, () -> {
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_get_product_info) + "\n" + e.getMessage());
                });
            }
        });
    }

    public void updateQuestionsUi(int idQuestionUpdated, QuestionAnswer newAnswer){
        ProductService.ProductDetails.Question q = _product.getQuestion(idQuestionUpdated);
        String oldAnswer = q.user_answer;

        if (newAnswer == QuestionAnswer.yes) {
            // It's impossible that the old answer was "yes"
            if (oldAnswer.equals("yes")) throw new RuntimeException("Invalid old answer");
            // Remove old answer
            if (oldAnswer.equals("no")) {
                q.num_no -= 1;
            }
            q.user_answer = "yes";
            q.num_yes += 1;
        }
        else if (newAnswer == QuestionAnswer.no) {
            // It's impossible that the old answer was "no"
            if (oldAnswer.equals("no")) throw new RuntimeException("Invalid old answer");
            if (oldAnswer.equals("yes")) {
                q.num_yes -= 1;
            }
            q.user_answer = "no";
            q.num_no += 1;
        }
        else if (newAnswer == QuestionAnswer.none) {
            if (oldAnswer.equals("yes")) {
                q.num_yes -= 1;
            }
            else if (oldAnswer.equals("no")) {
                q.num_no -= 1;
            }
            else {
                // It's impossible that the old answer was "none"
                throw new RuntimeException("Invalid old answer");
            }
            q.user_answer = "none";
        }

        _fragment.setQuestionsElements(_product.questions);
    }

    @Override
    public void reviewProduct() {
        if(stars == 0) {
            PopupMessage.warning(_fragment, _fragment.getString(R.string.missing_stars));
            return;
        }
        ExecutionThread.nonUI(() -> {
            try {
                ReviewService reviewService = ServiceFactory.getInstance().getReviewService();
                reviewService.reviewProduct(_productId, stars);
                updateReview();

            } catch (Exception e) {
                ExecutionThread.UI(_fragment, () -> {
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_add_review) + "\n" + e.getMessage());
                });
            }
        });
    }

    @Override
    public void answerQuestion(int questionId, QuestionAnswer answer){
        updateQuestionsUi(questionId, answer);
        ExecutionThread.nonUI(()->{
            try{
                QuestionService questionService = ServiceFactory.getInstance().getQuestionService();
                if (answer == QuestionAnswer.yes) {
                    questionService.answerQuestionProduct(_productId, questionId, true);
                }
                else if (answer == QuestionAnswer.no) {
                    questionService.answerQuestionProduct(_productId, questionId, false);
                }
                else {
                    questionService.removeQuestionProduct(_productId, questionId);
                }
            }
            catch (Exception e){
                ExecutionThread.UI(_fragment, () -> {
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_cast_vote) + "\n" + e.getMessage());
                });
            }
        });
    }

    @Override
    public int getPreviousReview() {
        if(_product == null){
            return 0;
        }
        return _product.userRate;
    }

    public void updateReview(){// _product.ratings_user;
        if(_product.userRate != 0) /* el usuario ha votado */{
            _product.ratings[_product.userRate]--;
        }
        _product.userRate = stars;
        _product.ratings[stars]++;
        ExecutionThread.UI(_fragment, () -> {
            _fragment.setAverageRating(_product.ratings);
        });
    }
}
