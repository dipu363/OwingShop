package com.dipuj2ee.owing.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.dipuj2ee.owing.model.BalanceModel;

public class SQLiteDBHandeler extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "owings.db";

    // Country table name
    private static final String TABLE_BALANCE_INFO = "balanceInfo";
    private static final String TABLE_NET_BALANCE = "netbalance";
   // private static final String TABLE_USER_IDINFO= "userFirebaseandCompanyid";

    // Country Table Columns names

    private static final String USERID = "userid";
    private static final String CUSTOMER_ID = "cusid";
    private static final String KEY_ID = "id";
    private static final String DR_BALANCE_TK = "drbalance";
    private static final String CR_BALANCE_TK = "crbalance";
    private static final String TRANSECTION_DATE = "trdate";


    public static final String CREATE_BALANCEINFO_TABLE = "CREATE TABLE " + TABLE_BALANCE_INFO + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USERID + " TEXT," + CUSTOMER_ID + " TEXT," + DR_BALANCE_TK + " DOUBLE,"
            + CR_BALANCE_TK + " DOUBLE," + TRANSECTION_DATE + " DATETIME" + ")";
    public static final String CREATE_NET_BALANCE_TABLE = "CREATE TABLE " + TABLE_NET_BALANCE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USERID + " TEXT," + CUSTOMER_ID + " TEXT," + DR_BALANCE_TK + " DOUBLE,"
            + CR_BALANCE_TK + " DOUBLE," + TRANSECTION_DATE + " DATETIME" + ")";


    public SQLiteDBHandeler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_BALANCEINFO_TABLE);
        db.execSQL(CREATE_NET_BALANCE_TABLE);
      //  db.execSQL(CREATE_IDINFO_TABLE);


    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.disableWriteAheadLogging();
        }
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BALANCE_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NET_BALANCE);
        // Create tables again
        onCreate(db);

    }


    public void  addBalanceInfo(BalanceModel blmodel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(KEY_ID,blmodel.getId());
        value.put(USERID,blmodel.getUserid());
        value.put(CUSTOMER_ID,blmodel.getCusid());
        value.put(DR_BALANCE_TK,blmodel.getDrBalance());
        value.put(CR_BALANCE_TK,blmodel.getCrBalance());
        value.put(TRANSECTION_DATE,blmodel.getTrdate());

        db.insert(TABLE_BALANCE_INFO,null,value);
        db.close();
    }


    public Cursor getindividualcusbalance(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        String [] columns= {
                SQLiteDBHandeler.CUSTOMER_ID,
                SQLiteDBHandeler.USERID,
                SQLiteDBHandeler.CUSTOMER_ID,
                SQLiteDBHandeler.DR_BALANCE_TK,
                SQLiteDBHandeler.CR_BALANCE_TK,
                SQLiteDBHandeler.TRANSECTION_DATE};

        Cursor cursor = db.query(SQLiteDBHandeler.TABLE_BALANCE_INFO,columns,SQLiteDBHandeler.CUSTOMER_ID+"='"+id+"'",null,null,null,null);

        return cursor ;



    }

    public Cursor getDebitTransection(String id){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("select * from " +
                        TABLE_BALANCE_INFO + " where " + CUSTOMER_ID + " = ? ", new String[] { id });
        return c ;

    }


    public void deleteFirstRaw(int row) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Deletes a row given its rowId, but I want to be able to pass
        // in the name of the KEY_NAME and have it delete that row.
        db.delete(TABLE_BALANCE_INFO, KEY_ID + "=" + row, null);
    }

    //table net balence crud
    public void addnetBalance(BalanceModel blmodel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(KEY_ID, blmodel.getId());
        value.put(USERID, blmodel.getUserid());
        value.put(CUSTOMER_ID, blmodel.getCusid());
        value.put(DR_BALANCE_TK, blmodel.getDrBalance());
        value.put(CR_BALANCE_TK, blmodel.getCrBalance());
        value.put(TRANSECTION_DATE, blmodel.getTrdate());

        db.insert(TABLE_NET_BALANCE, null, value);
        db.close();
    }

    public void updateNetBalance(BalanceModel blmodel, String cid) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(USERID, blmodel.getUserid());
        value.put(CUSTOMER_ID, blmodel.getCusid());
        value.put(DR_BALANCE_TK, blmodel.getDrBalance());
        value.put(CR_BALANCE_TK, blmodel.getCrBalance());
        value.put(TRANSECTION_DATE, blmodel.getTrdate());
        db.update(TABLE_NET_BALANCE, value, CUSTOMER_ID + "=?", new String[]{String.valueOf(cid)});

        //db.update(TABLE_NET_BALANCE,value,CUSTOMER_ID +" = ?",new String[] { cid });
        db.close();

    }

    public Cursor getnetbalence(String cusid) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("select * from " +
                TABLE_NET_BALANCE + " where " + CUSTOMER_ID + " = ? ", new String[]{cusid});
        return c;

    }

}
