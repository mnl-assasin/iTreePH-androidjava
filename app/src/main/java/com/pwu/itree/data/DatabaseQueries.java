package com.pwu.itree.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pwu.itree.model.GameScore;
import com.pwu.itree.model.Tree;

import java.util.ArrayList;
import java.util.List;

public class DatabaseQueries {

    private static String TAG = "TAG_DatabaseQueries";

    private static SQLiteDatabase getReadableSQL(Context ctx) {
        return DatabaseHelper.getInstance(ctx).getReadableDatabase();
    }

    private static SQLiteDatabase getWritableSQL(Context ctx) {
        return DatabaseHelper.getInstance(ctx).getWritableDatabase();
    }

    public static List<Tree> getFamilyTrees(Context ctx) {
        SQLiteDatabase db = getReadableSQL(ctx);
        List<Tree> list = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TBL_FAMILY, null, null, null, null, null, "scientificName asc");
        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String commonName = cursor.getString(cursor.getColumnIndex("commonName"));
            String scientificName = cursor.getString(cursor.getColumnIndex("scientificName"));
            int drawable = cursor.getInt(cursor.getColumnIndex("drawable"));

            list.add(new Tree(id, commonName, scientificName, drawable));
        }
        return list;
    }

    public static List<Tree> getSubTrees(Context ctx, int familyType) {

        List<Tree> trees = new ArrayList<>();
        Cursor cursor = getReadableSQL(ctx).query(DatabaseHelper.TBL_TREES, null, "familyType =?", new String[]{String.valueOf(familyType)}, null, null, "commonName asc");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String commonName = cursor.getString(cursor.getColumnIndex("commonName"));
            String scientificName = cursor.getString(cursor.getColumnIndex("scientificName"));
            int drawable = cursor.getInt(cursor.getColumnIndex("drawable"));
            trees.add(new Tree(id, commonName, scientificName, drawable));
        }

        cursor.close();
        return trees;
    }

    public static Tree getSubTree(Context ctx, int familyType, int id) {

        Cursor cursor = getReadableSQL(ctx).query(DatabaseHelper.TBL_TREES, null, "familyType =? AND id=?", new String[]{String.valueOf(familyType), String.valueOf(id)}, null, null, null);
        if (cursor.moveToNext()) {
            String commonName = cursor.getString(cursor.getColumnIndex("commonName"));
            String scientificName = cursor.getString(cursor.getColumnIndex("scientificName"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String habitat = cursor.getString(cursor.getColumnIndex("habitat"));
            String cultivationDetails = cursor.getString(cursor.getColumnIndex("cultivationDetails"));
            String otherUsage = cursor.getString(cursor.getColumnIndex("otherUsage"));
            int drawable = cursor.getInt(cursor.getColumnIndex("drawable"));
            return new Tree(id, commonName, scientificName, description, habitat, cultivationDetails, otherUsage, drawable);
        }

        cursor.close();
        return null;
    }

    public static Tree getFamilyTree(Context ctx, int familyType) {
        SQLiteDatabase db = getReadableSQL(ctx);

        Cursor cursor = db.query(DatabaseHelper.TBL_FAMILY, null, "id =?", new String[]{String.valueOf(familyType)}, null, null, null);
        if (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String commonName = cursor.getString(cursor.getColumnIndex("commonName"));
            String scientificName = cursor.getString(cursor.getColumnIndex("scientificName"));
            int drawable = cursor.getInt(cursor.getColumnIndex("drawable"));
            String description = cursor.getString(cursor.getColumnIndex("description"));

            return new Tree(id, commonName, scientificName, description, drawable);
        }

        cursor.close();
        return null;
    }

    public static void testDB(Context ctx) {

        String logString;
        SQLiteDatabase db = getReadableSQL(ctx);
//        Cursor cursor = db.query(TBL_TREES, null, "familyType =?", new String[]{String.valueOf(familyType)}, null, null, null);
        Cursor cursor = db.query(DatabaseHelper.TBL_TREES, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int familyType = cursor.getInt(cursor.getColumnIndex("familyType"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String commonName = cursor.getString(cursor.getColumnIndex("commonName"));
            String scientificName = cursor.getString(cursor.getColumnIndex("scientificName"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            String habitat = cursor.getString(cursor.getColumnIndex("habitat"));
            String cultivationDetails = cursor.getString(cursor.getColumnIndex("cultivationDetails"));
            String otherUsage = cursor.getString(cursor.getColumnIndex("otherUsage"));

            int drawable = cursor.getInt(cursor.getColumnIndex("drawable"));
//
            logString = "familyType: " + familyType +
                    "\nId: " + id +
                    "\ncommonName: " + commonName +
                    "\nscienfiticName: " + scientificName +
                    "\ndescription: " + description +
                    "\nhabitat: " + habitat +
                    "\ncultivationDetails: " + cultivationDetails +
                    "\notherUsage: " + otherUsage +
                    "\n\n";

//            logString = "description: " + description;
            Log.d(TAG, logString);
        }
    }

    public static void addScore(Context ctx, int type, GameScore score) {
        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put("name", score.getName());
        values.put("score", score.getScore());
        getWritableSQL(ctx).insert("TBL_SCORES", null, values);
        getWritableSQL(ctx).close();
    }

    public static List<GameScore> getHighScore(Context ctx, int type) {

        List<GameScore> scores = new ArrayList<>();
        SQLiteDatabase db = getReadableSQL(ctx);
        Cursor cursor = db.query("TBL_SCORES", null, "type=?", new String[]{String.valueOf(type)}, null, null, "score DESC");
        Log.d(TAG, "Size: " + cursor.getCount());
//        if (cursor.moveToNext())
        while (cursor.moveToNext())
            scores.add(new GameScore(cursor.getString(cursor.getColumnIndex("name")), cursor.getInt(cursor.getColumnIndex("score"))));
        cursor.close();
        return scores;
    }


}
