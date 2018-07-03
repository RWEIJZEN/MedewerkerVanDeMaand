package nl.weijzen.maastricht.medewerkervandemaand;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller3SharePicture extends AppCompatActivity {
    private Menu menu;
    private ShareActionProvider shareActionProvider = null;
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
        setMenuName(R.string.menu_title_view_3_share_picture);
        getMenuInflater().inflate(R.menu.menu_view_3_share_picture, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menuitem_select_eotm:
                startActivityForResult(getAvailableFileSelectors(), 200);
                return true;
            case R.id.menuitem_share_eotm:
                MenuItem menuItemShare = menu.findItem(R.id.menuitem_share_eotm);
                shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItemShare);
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
                showPictureInImageView(R.id.imageView3SharePicture);
            } else {
                Resources resource = getResources();
                Bitmap noInput = ((BitmapDrawable) getDrawable(R.drawable.ic_launcher_background)).getBitmap();
                showPictureInImageView(R.id.imageView3SharePicture);
            }
        }
    }

    // onOptionsItemSelected -----------------------------------------------------------------------
    private Intent getAvailableFileSelectors(){
        List<Intent> allIntents = getIntentsToChoose();
        allIntents = removefallbackIntentFromAllIntentsList(allIntents);
        Intent mainIntent = extractMainIntentFromAllIntentsList(allIntents);
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        return chooserIntent;
    }

    // getAvailableFileSelectors (onOptionsItemSelected) -------------------------------------------
    private List<Intent> getIntentsToChoose() {
        List<Intent> allIntents = addAllGalleryIntentsToAllIntentsList();
        return allIntents;
    }

    // getIntentsToChoose (onOptionsItemSelected/getAvailableFileSelectors) ------------------------
    private List<Intent> addAllGalleryIntentsToAllIntentsList() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        List<Intent> allIntents = new ArrayList<>();
        for (ResolveInfo res : listGallery) {
            Intent intent = buildIntent(galleryIntent, res);
            allIntents.add(intent);
        }
        return allIntents;
    }

    // addAllGalleryIntentsToAllIntentsList (onOptionsItemSelected/getAvailableFileSelectors/getIntentsToChoose) -----
    private Intent buildIntent(Intent targetIntent, ResolveInfo resolveInfo) {
        Intent intent = new Intent(targetIntent);
        intent.setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
        intent.setPackage(resolveInfo.activityInfo.packageName);
        return intent;
    }

    // getAvailableFileSelectors (onOptionsItemSelected) -------------------------------------------
    private List<Intent> removefallbackIntentFromAllIntentsList(List<Intent> allIntents) {
        Intent fallbackIntent = null;
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.fallback.Fallback")) {
                fallbackIntent = intent;
            }
        }
        allIntents.remove(fallbackIntent);
        return allIntents;
    }

    // getAvailableFileSelectors (onOptionsItemSelected) -------------------------------------------
    private Intent extractMainIntentFromAllIntentsList(List<Intent> allIntents) {
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
            }
        }
        allIntents.remove(mainIntent);
        return mainIntent;
    }

    // onCreateOptionsMenu -------------------------------------------------------------------------
    private void setMenuName(int recourceId){
        setTitle(getResourceString(recourceId));
    }


    // setMenuName (onCreateOptionsMenu) -----------------------------------------------------------
    private String getResourceString(int RecourceId) {
        Resources resources = getResources();
        String result = resources.getString(RecourceId);
        return result;
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
    private void showPictureInImageView(int imageViewId) {
        ImageView imageViewSelectPicture = findViewById(imageViewId);
        imageViewSelectPicture.setImageBitmap(picture.getBitmap());
    }

    // onActivityResult ----------------------------------------------------------------------------
    private Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return Uri.parse(data.getData().toString());
    }
}
