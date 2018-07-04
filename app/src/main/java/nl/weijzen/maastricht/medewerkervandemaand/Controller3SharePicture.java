package nl.weijzen.maastricht.medewerkervandemaand;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.Objects;

public class Controller3SharePicture extends AppCompatActivity {
    private Picture picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_3_share_picture);

        picture = showCompoundPictureInShareView();
    }

    // ActionBar menu overrides
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setMenuName();
        getMenuInflater().inflate(R.menu.menu_view_3_share_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menuitem_select_eotm:
                Intent picIntent = new Intent(Intent.ACTION_GET_CONTENT,null);
                picIntent.setType("image/*");
                picIntent.putExtra("return_data",true);
                startActivityForResult(picIntent,1);
                return true;
            case R.id.menuitem_share_eotm:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResourceString(R.string.share_subject));
                shareIntent.putExtra(Intent.EXTRA_TEXT,    getResourceString(R.string.share_body));
                shareIntent.putExtra(Intent.EXTRA_STREAM,  picture.getPictureUri());
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "Share this text"));
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri picUri = getPickImageResultUri(data);
            if (picUri != null) {
                picture = new Picture(this, picUri);
                showPictureInImageView();
            } else {
                showPictureInImageView();
            }
        }
    }

    // onCreateOptionsMenu -------------------------------------------------------------------------
    private void setMenuName(){
        setTitle(getResourceString(R.string.menu_title_view_3_share_picture));
    }

    // setMenuName (onCreateOptionsMenu) -----------------------------------------------------------
    private String getResourceString(int RecourceId) {
        Resources resources = getResources();
        return resources.getString(RecourceId);
    }

    // Controller3SharePicture ---------------------------------------------------------------------
    private Picture showCompoundPictureInShareView(){
        Picture picture = loadPictureFromUriTextInIntent();
        if (picture != null){
            ImageView imageView = findViewById(R.id.imageView3SharePicture);
            imageView.setImageBitmap(picture.getBitmap());
        }
        return picture;
    }

    // showCompoundPictureInShareView (Controller3SharePicture) ------------------------------------
    private Picture loadPictureFromUriTextInIntent() {
        Intent intent = getIntent();
        String pictureUriText = intent.getStringExtra("COMPOUNDPICTURE_URI_TEXT");
        if (pictureUriText != null) {
            return new Picture(this, pictureUriText);
        }
        return null;
    }

    // onActivityResult ----------------------------------------------------------------------------
    private void showPictureInImageView() {
        ImageView imageViewSelectPicture = findViewById(R.id.imageView3SharePicture);
        imageViewSelectPicture.setImageBitmap(picture.getBitmap());
    }

    // onActivityResult ----------------------------------------------------------------------------
    private Uri getPickImageResultUri(Intent data) {
        return Uri.parse(Objects.requireNonNull(data.getData()).toString());
    }
}