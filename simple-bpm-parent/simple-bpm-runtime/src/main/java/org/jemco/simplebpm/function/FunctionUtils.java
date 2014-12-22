package org.jemco.simplebpm.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class FunctionUtils {

	public static <T> T retrieveObjectFromCollection(Collection<T> items, Predicate<T> predicate) 
	{
		
		for (T item : items) {
			
			if (predicate.accept(item)) {
				return item;
			}
			
		}
		
		return null;
		
	}
	
	public static <T> T retrieveObjectFromCollection(Predicate<T> predicate, Retriever<T> retriever) 
	{
		
		Collection<T> items = retriever.retrive();
		for (T item : items) {
			
			if (predicate.accept(item)) {
				return item;
			}
			
		}
		
		return null;
		
	}
	
	public static <T> Collection<T> predicateCollection(Predicate<T> predicate, Retriever<T> retriever) 
	{
		
		ArrayList<T> newList = new ArrayList<T>();
		Collection<T> items = retriever.retrive();
		for (T item : items) {
			
			if (predicate.accept(item)) {
				newList.add(item);
			}
			
		}
		
		return newList;
		
	}
	
	public static <T> List<T> predicateCollection(List<T> items, Predicate<T> predicate) 
	{
		
		ArrayList<T> newList = new ArrayList<T>();
		for (T item : items) {
			
			if (predicate.accept(item)) {
				newList.add(item);
			}
			
		}
		
		return newList;
		
	}
	
}
