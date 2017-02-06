package zad1;

import java.lang.reflect.*;
import java.util.*;

import javax.swing.table.*;

public class GenTabModel<T> extends AbstractTableModel
{
	private static final long serialVersionUID = 7132738243337850213L;
	private List<T> rows;
	private Field[] fields;
	private String[] cnames;

	public GenTabModel(List<T> rows, String[] fnames, String[] cnames)
	{
		super();
		if (rows.size() == 0)
			throw new IllegalArgumentException("Empty objects list");
		Class<?> klas = rows.get(0).getClass();
		fields = new Field[fnames.length];
		try
		{
			for (int i = 0; i < fnames.length; i++)
			{
				fields[i] = klas.getDeclaredField(fnames[i]);
				fields[i].setAccessible(true);
			}
		} catch (NoSuchFieldException exc)
		{
			IllegalArgumentException ilae = new IllegalArgumentException(
					"Field not found", exc);
			throw ilae;
		}
		this.rows = rows;
		this.cnames = cnames;
	}

	@Override
	public int getColumnCount()
	{
		return cnames.length;
	}

	@Override
	public int getRowCount()
	{
		return rows.size();
	}

	@Override
	public String getColumnName(int c)
	{
		return cnames[c];
	}

	@Override
	public Class<?> getColumnClass(int c)
	{
		try
		{
			return fields[c].get(rows.get(0)).getClass();
		} catch (Exception exc)
		{
			exc.printStackTrace();
			return Object.class;
		}
	}

	@Override
	public Object getValueAt(int r, int c)
	{
		T obj = rows.get(r);
		try
		{
			return fields[c].get(obj);
		} catch (IllegalArgumentException exc)
		{
			exc.printStackTrace();
			return null;
		} catch (IllegalAccessException exc)
		{
			exc.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}

	@Override
	public void setValueAt(Object value, int r, int c)
	{
		T obj = rows.get(r);
		try
		{
			fields[c].set(obj, value);
		} catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}

}