package com.example.android.katsapp;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.katsapp.database.BreedsDatabase;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private String LOG_TAG = WidgetRemoteViewsFactory.class.getSimpleName();
    private Cursor mCursor;

    public WidgetRemoteViewsFactory(Context applicationContext, Intent intent){
        this.mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        if (mCursor != null){
            mCursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        /*
        Uri uri = BreedsEntry.CONTENT_URI;

        mCursor = mContext.getContentResolver().query(uri, null, null,
                null, "_id DESC");
         */

        BreedsDatabase mDb = BreedsDatabase.getInstance(mContext);

        String query = "SELECT * FROM breeds ORDER BY _id DESC LIMIT 1";

        mCursor = mDb.query(query, null);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

        if (mCursor != null){
            mCursor.close();
        }

    }

    @Override
    public int getCount() {
        // return mCursor == null ? 0 : mCursor.getCount();

        return 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (position == AdapterView.INVALID_POSITION || mCursor == null || !mCursor.moveToPosition(position)){
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.cats_widget_list_item);

        rv.setTextViewText(R.id.widget_title, mCursor.getString(2));
        rv.setTextViewText(R.id.temperament, mCursor.getString(7));

        Intent fillInIntent = new Intent();

        rv.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
