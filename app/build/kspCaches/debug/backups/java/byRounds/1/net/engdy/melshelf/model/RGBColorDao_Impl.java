package net.engdy.melshelf.model;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RGBColorDao_Impl implements RGBColorDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RGBColor> __insertionAdapterOfRGBColor;

  private final EntityDeletionOrUpdateAdapter<RGBColor> __deletionAdapterOfRGBColor;

  private final EntityDeletionOrUpdateAdapter<RGBColor> __updateAdapterOfRGBColor;

  public RGBColorDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRGBColor = new EntityInsertionAdapter<RGBColor>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `rgb_colors` (`id`,`name`,`red`,`green`,`blue`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RGBColor entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getRed());
        statement.bindLong(4, entity.getGreen());
        statement.bindLong(5, entity.getBlue());
      }
    };
    this.__deletionAdapterOfRGBColor = new EntityDeletionOrUpdateAdapter<RGBColor>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `rgb_colors` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RGBColor entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRGBColor = new EntityDeletionOrUpdateAdapter<RGBColor>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `rgb_colors` SET `id` = ?,`name` = ?,`red` = ?,`green` = ?,`blue` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RGBColor entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getRed());
        statement.bindLong(4, entity.getGreen());
        statement.bindLong(5, entity.getBlue());
        statement.bindLong(6, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final RGBColor rgbColor, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRGBColor.insert(rgbColor);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final RGBColor rgbColor, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRGBColor.handle(rgbColor);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final RGBColor rgbColor, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRGBColor.handle(rgbColor);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RGBColor>> getAllColors() {
    final String _sql = "SELECT * FROM rgb_colors";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rgb_colors"}, new Callable<List<RGBColor>>() {
      @Override
      @NonNull
      public List<RGBColor> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfRed = CursorUtil.getColumnIndexOrThrow(_cursor, "red");
          final int _cursorIndexOfGreen = CursorUtil.getColumnIndexOrThrow(_cursor, "green");
          final int _cursorIndexOfBlue = CursorUtil.getColumnIndexOrThrow(_cursor, "blue");
          final List<RGBColor> _result = new ArrayList<RGBColor>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RGBColor _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpRed;
            _tmpRed = _cursor.getInt(_cursorIndexOfRed);
            final int _tmpGreen;
            _tmpGreen = _cursor.getInt(_cursorIndexOfGreen);
            final int _tmpBlue;
            _tmpBlue = _cursor.getInt(_cursorIndexOfBlue);
            _item = new RGBColor(_tmpId,_tmpName,_tmpRed,_tmpGreen,_tmpBlue);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<RGBColor> getColorById(final int id) {
    final String _sql = "SELECT * FROM rgb_colors WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rgb_colors"}, new Callable<RGBColor>() {
      @Override
      @NonNull
      public RGBColor call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfRed = CursorUtil.getColumnIndexOrThrow(_cursor, "red");
          final int _cursorIndexOfGreen = CursorUtil.getColumnIndexOrThrow(_cursor, "green");
          final int _cursorIndexOfBlue = CursorUtil.getColumnIndexOrThrow(_cursor, "blue");
          final RGBColor _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpRed;
            _tmpRed = _cursor.getInt(_cursorIndexOfRed);
            final int _tmpGreen;
            _tmpGreen = _cursor.getInt(_cursorIndexOfGreen);
            final int _tmpBlue;
            _tmpBlue = _cursor.getInt(_cursorIndexOfBlue);
            _result = new RGBColor(_tmpId,_tmpName,_tmpRed,_tmpGreen,_tmpBlue);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
