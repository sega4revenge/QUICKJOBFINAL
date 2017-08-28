package za.co.riggaroo.materialhelptutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import static android.app.Activity.RESULT_OK;


public class EndPagerIntro extends Fragment {
    private static final String ARG_TUTORIAL_ITEM = "arg_tut_item";
    private static final String TAG = "MaterialTutFragment";
    private static final String ARG_PAGE = "arg_page";

    public static EndPagerIntro newInstance(TutorialItem tutorialItem, int page) {
        EndPagerIntro helpTutorialImageFragment = new EndPagerIntro();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TUTORIAL_ITEM, tutorialItem);
        args.putInt(ARG_PAGE, page);
        helpTutorialImageFragment.setArguments(args);
        return helpTutorialImageFragment;
    }

    private TutorialItem tutorialItem;
    int page;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        tutorialItem = b.getParcelable(ARG_TUTORIAL_ITEM);
        page = b.getInt(ARG_PAGE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_end_pager_intro, container, false);
        v.setTag(page);

        ImageView imageViewBack = (ImageView) v.findViewById(R.id.fragment_help_tutorial_imageview_background);
        ImageView imageViewBack2 = (ImageView) v.findViewById(R.id.fragment_help_tutorial_imageview_background2);
        Button btFind = (Button) v.findViewById(R.id.mButtonQJFind);
        Button btHire = (Button) v.findViewById(R.id.mButtonQJHire);
        btFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setResult(RESULT_OK);
                getActivity().finish();
            }
        });
        btHire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setResult(999);
                getActivity().finish();
            }
        });
        if (tutorialItem.getBackgroundImageRes() != -1) {
            Glide.with(this).load(tutorialItem.getBackgroundImageRes()).into(imageViewBack);
        }
        if (tutorialItem.getForegroundImageRes() != -1) {
            Glide.with(this).load(tutorialItem.getForegroundImageRes()).into(imageViewBack2);
        }
        return v;
    }

  /*  public void onTranform(int pageNumber, float transformation) {
        Log.d(TAG, "onTransform:" + transformation);
        if (!isAdded()) {
            return;
        }
        if (transformation == 0){
            imageViewBack.setTranslationX(0);
            return;
        }
        int pageWidth = getView().getWidth();
        Log.d(TAG, "onTransform Added page Width:" + pageWidth);

        imageViewBack.setTranslationX(-transformation * (pageWidth / 2)); //Half the normal speed

    }*/
}
