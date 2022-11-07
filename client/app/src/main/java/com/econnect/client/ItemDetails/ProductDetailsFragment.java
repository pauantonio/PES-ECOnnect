package com.econnect.client.ItemDetails;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.econnect.API.ProductService.ProductDetails.Question;
import com.econnect.Utilities.CustomFragment;
import com.econnect.client.R;
import com.econnect.client.databinding.FragmentProductDetailsBinding;

public class ProductDetailsFragment extends CustomFragment<FragmentProductDetailsBinding> {

    private IDetailsController _ctrl;
    private ImageView star1Rpopup, star2Rpopup, star3Rpopup, star4Rpopup, star5Rpopup;


    public ProductDetailsFragment() {
        super(FragmentProductDetailsBinding.class);
    }

    public void setController(IDetailsController ctrl) {
        this._ctrl = ctrl;
    }

    @Override
    protected void addListeners() {
        if (_ctrl == null) return;
        binding.addRatingButton.setOnClickListener(view -> createReviewDialog());
        _ctrl.updateUIElements();
    }

    void question(int qId, IDetailsController.QuestionAnswer answer) {
        _ctrl.answerQuestion(qId, answer);
    }


    public void updateStars(int nStars){
        final Drawable fullStar = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_star_24);
        final Drawable emptyStar = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_star_empty_24);

        star1Rpopup.setImageDrawable(nStars < 1 ? emptyStar : fullStar);
        star2Rpopup.setImageDrawable(nStars < 2 ? emptyStar : fullStar);
        star3Rpopup.setImageDrawable(nStars < 3 ? emptyStar : fullStar);
        star4Rpopup.setImageDrawable(nStars < 4 ? emptyStar : fullStar);
        star5Rpopup.setImageDrawable(nStars < 5 ? emptyStar : fullStar);
    }

    void setAverageRating(int[] votes) {
        final Drawable fullStar = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_star_24);
        final Drawable halfStar = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_star_half_24);
        final Drawable emptyStar = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_star_empty_24);
        final Drawable[] starDrawables = new Drawable[]{emptyStar, halfStar, fullStar};
        
        int rating = 0;
        int numVotes = 0;
        for (int i = 0; i < votes.length; i++) {
            rating += votes[i] * i;
            numVotes += votes[i];
        }
        float average = ((float) rating) / numVotes;

        int[] stars = new int[5]; // 0=empty, 1=half, 2=full
        // Set values of stars according to average
        for (int i = 0; i < 5; i++) {
            if (average >= i + 1) {
                stars[i] = 2;
            } else if (average >= i + 0.5) {
                stars[i] = 1;
            } else {
                stars[i] = 0;
            }
        }
        binding.star1.setImageDrawable(starDrawables[stars[0]]);
        binding.star2.setImageDrawable(starDrawables[stars[1]]);
        binding.star3.setImageDrawable(starDrawables[stars[2]]);
        binding.star4.setImageDrawable(starDrawables[stars[3]]);
        binding.star5.setImageDrawable(starDrawables[stars[4]]);

        // Set text for number of votes
        if (numVotes == 1) {
            binding.numberOfRatingsText.setText(getResources().getString(R.string.num_votes_text_one, numVotes));
        } else {
            binding.numberOfRatingsText.setText(getResources().getString(R.string.num_votes_text, numVotes));
        }
    }

    void setImage(Bitmap bmp) {
        binding.productImage.setImageBitmap(bmp);
    }

    void setQuestionsElements(Question[] questions) {
        int highlightColor = ContextCompat.getColor(requireContext(), R.color.green);
        QuestionListAdapter adapter = (QuestionListAdapter) binding.questionsList.getAdapter();
        if (adapter == null) {
            binding.questionsList.setAdapter(new QuestionListAdapter(this, questions, highlightColor));
        }
        else {
            adapter.replaceData(questions);
        }
    }

    void setTitle(String name) {
        binding.productNameText.setText(name);
    }

    private void createReviewDialog(){
        AlertDialog.Builder reviewBuilder = new AlertDialog.Builder(getContext());

        final View reviewPopupView = getLayoutInflater().inflate(R.layout.reviewpopup, null);

        star1Rpopup = reviewPopupView.findViewById(R.id.star1Rpopup);
        star2Rpopup = reviewPopupView.findViewById(R.id.star2Rpopup);
        star3Rpopup = reviewPopupView.findViewById(R.id.star3Rpopup);
        star4Rpopup = reviewPopupView.findViewById(R.id.star4Rpopup);
        star5Rpopup = reviewPopupView.findViewById(R.id.star5Rpopup);

        reviewBuilder.setView(reviewPopupView);
        final AlertDialog review = reviewBuilder.create();
        review.show();

        star1Rpopup.setOnClickListener(view -> _ctrl.setStars(1));
        star2Rpopup.setOnClickListener(view -> _ctrl.setStars(2));
        star3Rpopup.setOnClickListener(view -> _ctrl.setStars(3));
        star4Rpopup.setOnClickListener(view -> _ctrl.setStars(4));
        star5Rpopup.setOnClickListener(view -> _ctrl.setStars(5));

        // Reset stars
        int prevNumStars = _ctrl.getPreviousReview();
        _ctrl.setStars(prevNumStars);

        Button reviewpopup_cancel = reviewPopupView.findViewById(R.id.reviewpopup_cancel);
        reviewpopup_cancel.setOnClickListener(view -> review.dismiss());

        Button reviewpopup_submit = reviewPopupView.findViewById(R.id.reviewpopup_submit);
        reviewpopup_submit.setOnClickListener(view -> {
            _ctrl.reviewProduct();
            review.dismiss();
        });
    }

    void showMapButton(View.OnClickListener listener) {
        binding.viewMapButtonDetails.setVisibility(View.VISIBLE);
        binding.viewMapButtonDetails.setOnClickListener(listener);
    }

}