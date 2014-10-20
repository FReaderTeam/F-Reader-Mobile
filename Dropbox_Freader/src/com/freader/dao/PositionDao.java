package com.freader.dao;
 
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxDatastoreManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;
import com.freader.utils.ToastUtils;
 
import android.content.Context;
import static com.freader.dao.DropboxSettings.*;
 
public class PositionDao {
 
    private DbxAccountManager mAccountManager;
    private DbxDatastoreManager mDatastoreManager;
    private DbxDatastore datastore;
    private static final String BOOK_PATH_FIELD_NAME = "bookFullPath";
    private static final String PARAGRAPH_FIELD_NAME = "paragraph";
    private static final String TABLE_NAME = "position";
    private static PositionDao instance;
     
    public static synchronized PositionDao getInstance(Context appContext) throws DbxException{
        if(instance == null){
            return instance = new PositionDao(appContext);
        }
        return instance;
    }
     
    private PositionDao(Context appContext) throws DbxException{
        // Set up the account manager
        mAccountManager = DbxAccountManager.getInstance(appContext, APP_KEY, APP_SECRET);
 
        // Set up the datastore manager
        if (mAccountManager.hasLinkedAccount()) {
            try {
                // Use Dropbox datastores
                mDatastoreManager = DbxDatastoreManager.forAccount(mAccountManager.getLinkedAccount());
            } catch (DbxException.Unauthorized e) {
                ToastUtils.showShortToast(appContext,"Account was unlinked remotely");
            }
        }
        if (mDatastoreManager == null) {
            // Account isn't linked yet, use local datastores
            mDatastoreManager = DbxDatastoreManager.localManager(mAccountManager);
        }
        datastore = mDatastoreManager.openDefaultDatastore();
         
    }
     
    public void savePosition(String bookFullPath, long absoluteParagraph) throws DbxException{
        DbxTable positionTable = datastore.getTable(TABLE_NAME);
        positionTable.insert()
        .set(BOOK_PATH_FIELD_NAME, bookFullPath)
        .set(PARAGRAPH_FIELD_NAME, absoluteParagraph);
        datastore.sync();
    }
     
    public long getPosition(String bookFullPath) throws DbxException{
        DbxTable positionTable = datastore.getTable(TABLE_NAME);
        DbxFields queryParams = new DbxFields().set(BOOK_PATH_FIELD_NAME, bookFullPath);
        DbxTable.QueryResult results = positionTable.query(queryParams);
        DbxRecord firstResult = results.iterator().next();
        return firstResult.getLong(PARAGRAPH_FIELD_NAME);
    }
     
}