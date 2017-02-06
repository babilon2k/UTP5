package zad1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Controller
{
	private Class<?> modelClass;
	private Object modelObject;
	private int years;
	private int LL;

	public Controller(String model)
	{
		try
		{
			modelClass = Class.forName("zad1.models." + model);
			modelObject = modelClass.newInstance();

		} catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		} catch (InstantiationException e)
		{
			throw new RuntimeException(e);
		} catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		} catch (SecurityException e)
		{
			throw new RuntimeException(e);
		}
		// new Gui();
	}

	public Controller readDataFrom(String data)
	{
		File file = new File(data);
		String line;
		if (file.exists() && file.isFile() && file.canRead())
		{
			try (BufferedReader br = new BufferedReader(new FileReader(data)))
			{
				while ((line = br.readLine()) != null)
				{
					StringTokenizer st = new StringTokenizer(line);
					String s = st.nextToken();
					List<Double> list = new ArrayList<Double>();
					while (st.hasMoreTokens())
						list.add(new Double(st.nextToken()));
					if (s.equals("LATA"))
					{
						LL = list.size();
						years = list.get(0).intValue();
						Field f = modelClass.getDeclaredField("LL");
						f.setAccessible(true);
						f.set(modelObject, LL);
					} else
					{
						double arr[] = new double[LL];
						for (int i = 0; i < list.size(); i++)
							arr[i] = list.get(i);
						for (int i = list.size(); i < LL; i++)
							arr[i] = list.get(list.size() - 1);

						Field f = modelClass.getDeclaredField(s);
						f.setAccessible(true);
						f.set(modelObject, arr);
					}
				}
			} catch (FileNotFoundException e)
			{
				throw new RuntimeException(e);
			} catch (IOException e)
			{
				throw new RuntimeException(e);
			} catch (NoSuchFieldException e)
			{
				throw new RuntimeException(e);
			} catch (SecurityException e)
			{
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e)
			{
				throw new RuntimeException(e);
			} catch (IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}

		}
		return this;
	}

	public Controller runModel()
	{
		try
		{
			modelClass.getMethod("run").invoke(modelObject);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException
				| SecurityException e)
		{
			throw new RuntimeException(e);
		}

		return this;
	}

	public Controller runScriptFromFile(String file)
	{
		try (FileReader fr = new FileReader(file))
		{
			runScriptFromReader(fr);
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		return this;
	}

	public Controller runScript(String script)
	{
		runScriptFromReader(new StringReader(script));
		return this;
	}

	public String getResultsAsTsv()
	{
		StringBuffer res = new StringBuffer();
		res.append("LATA");
		res.append('\t');
		for (int i = 0; i < LL; i++)
		{
			res.append(years + i);
			res.append('\t');
		}
		res.append('\n');

		for (Field f : getBoundFileds())
		{
			if (!f.getName().equals("LL"))
			{
				try
				{
					f.setAccessible(true);
					Object obj = f.get(modelObject);
					res.append(f.getName());
					res.append('\t');
					for (int j = 0; j < Array.getLength(obj); j++)
					{
						res.append(Array.get(obj, j));
						res.append('\t');
					}
					res.append('\n');
				} catch (IllegalArgumentException | IllegalAccessException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		return res.toString();
	}

	private ArrayList<Field> getBoundFileds()
	{
		ArrayList<Field> boundFileds = new ArrayList<>();
		for (Field f : modelClass.getDeclaredFields())
		{
			if (f.isAnnotationPresent(Bind.class))
			{
				boundFileds.add(f);
			}
		}
		return boundFileds;
	}

	public void runScriptFromReader(Reader reader)
	{
		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine groovyEngine = engineManager.getEngineByName("groovy");
		if (groovyEngine == null)
		{
			throw new RuntimeException("No engine found for groovy");
		}
		Bindings b = groovyEngine.createBindings();
		for (Field f : getBoundFileds())
		{
			try
			{
				f.setAccessible(true);
				b.put(f.getName(), f.get(modelObject));
			} catch (IllegalArgumentException | IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
		}
		try
		{
			groovyEngine.eval(reader, b);
		} catch (ScriptException e)
		{
			throw new RuntimeException(e);
		}
	}

}