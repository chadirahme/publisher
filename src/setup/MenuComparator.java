package setup;

import java.io.Serializable;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import domains.MenuModel;

import org.zkoss.zul.GroupComparator;

public class MenuComparator implements Comparator<MenuModel>, GroupComparator<MenuModel>, Serializable {
    private static final long serialVersionUID = 1L;
 
    public int compare(MenuModel o1, MenuModel o2) {
        return o1.getArtitle().compareTo(o2.getArtitle().toString());
    }
 
    public int compareGroup(MenuModel o1, MenuModel o2) {
        if(o1.getArtitle().equals(o2.getArtitle()))
            return 0;
        else
            return 1;
    }

	@Override
	public Comparator<MenuModel> reversed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparator<MenuModel> thenComparing(
			Comparator<? super MenuModel> other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <U> Comparator<MenuModel> thenComparing(
			Function<? super MenuModel, ? extends U> keyExtractor,
			Comparator<? super U> keyComparator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <U extends Comparable<? super U>> Comparator<MenuModel> thenComparing(
			Function<? super MenuModel, ? extends U> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparator<MenuModel> thenComparingInt(
			ToIntFunction<? super MenuModel> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparator<MenuModel> thenComparingLong(
			ToLongFunction<? super MenuModel> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparator<MenuModel> thenComparingDouble(
			ToDoubleFunction<? super MenuModel> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
}

