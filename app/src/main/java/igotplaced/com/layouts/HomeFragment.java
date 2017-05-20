package igotplaced.com.layouts;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;


public class HomeFragment extends Fragment {

    private Context context;
    private VideoView videoView;
    private Toolbar toolbar;
    private MediaController mediaController;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        context = getContext();
/*        toolbar = (Toolbar) view.findViewById(R.id.MyToolbar);
        if ((getActivity()).getActionBar() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/

        videoView = (VideoView) view.findViewById(R.id.video_header);
        mediaController = new MediaController(context);
        mediaController.setAnchorView(videoView);
        mediaController.hide();
        Uri uri = Uri.parse("http://r5---sn-a5mekn7k.googlevideo.com/videoplayback?id=o-AEO9ty-F17hi-ZZxsi8L-srbOp_8cLQ5YeRWfhHh7mkj&expire=1495293746&mime=video%2Fmp4&key=cms1&ip=117.246.253.231&lmt=1462068731445939&sparams=dur,ei,expire,id,initcwndbps,ip,ipbits,ipbypass,itag,lmt,mime,mip,mm,mn,ms,mv,pl,ratebypass,source,upn&ei=0gogWYT8F8nDY7jlk4gF&itag=18&dur=54.056&pl=20&ipbits=0&ratebypass=yes&source=youtube&signature=5503BB19676EF3D69FA9A014E5E65D7CEA87D7E0.82ADC3CA7C5CA297BDE3AC13866DCC8B2A65AE54&upn=E9_VAw-YmCQ&title=How+it+works+-+iGotPlaced+Social+Placement+Preparation+Explained%21&req_id=bb05ae0186c7a3ee&cm2rm=sn-n8vz6ee&mip=117.246.197.153&redirect_counter=3&cms_redirect=yes&ipbypass=yes&mm=34&mn=sn-a5mekn7k&ms=ltu&mt=1495272075&mv=m");
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.seekTo(9000);

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
                mediaController.show();
            }
        });

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle("iGotPlaced");
        collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(context, R.color.white));
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(context, R.color.white));


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
