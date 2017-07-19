package com.sh;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class Monopoly {
	
	
	
	
	public static void main(String[] args)
	{
		Monopoly.exec();
	}
	
	
	
	/*  assumes that you always pay to get out of jail
		no get out of jail free card
		chance cards have the potential to be repeated: aren't removed from deck
	 */
	public static void exec()
	{
		Integer i,roll1,roll2,sum,place;
		// rolls for doubles
		Integer  roll3, roll4, roll5, roll6, roll7, roll8;
		Integer chance, commChest;
		
		List<StreetEntity> ls =  Monopoly.readJSON();
		
		place = 0;
		
		for(i=1;i<1000000;i++)
		{
			roll1 = 0;
			roll2 = 0;
			sum = 0;

			roll1 = (int) (Math.random()*6)+1;
			roll2 = (int) (Math.random()*6)+1;
			sum = roll1 + roll2;
			
			
			// =========== account for rolling doubles =============!
			if (roll1 == roll2)	
			{
				roll3 = (int) (Math.random()*6)+1;
				roll4 = (int) (Math.random()*6)+1;
				sum = sum + roll3 + roll4;
				
				if (roll3 == roll4)
				{
				
					roll5 = (int) (Math.random()*6)+1;
					roll6 = (int) (Math.random()*6)+1;
					sum = sum + roll5 + roll6;
				
					if (roll5 == roll6) 
					{
					
						place = 10;
						roll7 = (int) (Math.random()*6)+1;
						roll8 = (int) (Math.random()*6)+1;
						sum = roll7 + roll8;
					}
				
				}
			
			} 	
			
			// =====================================================!	
			
			if ((sum+place) < 40)
			{
				place = place + sum;
			}
			else if ((sum+place) >= 40)
			{
				place = place + sum - 40;
			}
			
			
			
			
			// =================== chance cards ======================!

			if ((place == 7) || (place == 22) || (place == 36))
			{
				chance = (int) (Math.random()*16)+1;
				
				if (chance == 1)
				{
					place = 0;				// advance to GO
					
				}
				else if (chance == 2)
				{
					place = 23;				// advance to Illinois Ave
					
				}
				else if (chance == 3)
				{
					place = 11;				// advance to St Charles Place
					
				}
				else if (chance == 4)
				{
					if ((place == 7) || (place == 36))
					{
						place = 12;
						
					}
					else if (place == 22)
					{
						place = 28;
						
					}
				}					// advance to nearest utility
				else if (chance == 5) 
				{
					if (place == 7) 
					{
						place = 15;
						
					}
					else if (place == 22)
					{
						place = 25;	
						
					}
					else if (place == 36)
					{
						place = 5; 
						 // advance to nearest railroad
					}	
				}
				else if (chance == 6)
				{
					place = place - 3;		// go back 3 spaces
					
				}
				else if (chance == 7)
				{
					place = 10;				// go directly to jail
					
				}
				else if (chance == 8)
				{
					place = 5;				// go to Reading Railroad
					
				}
				else if (chance == 9)
				{
					place = 39;				// go to Boardwalk
					
				}
				else if (chance > 9) 
				{
					place = place + 0;		// all other chance cards
				}
			
			}
			
	//=================== community chest cards ========================!	

			if ((place == 2) || (place == 17) || (place == 33))
			{
			
				commChest = (int) (Math.random()*16)+1;
				
				if (commChest == 1) 
				{
					place = 0;				// advance to GO
					
				}
				else if (commChest == 2)
				{
					place = 10;				// go directly to jail
					
				}
				else if (commChest > 2) 
				{
					place = place + 0;
				}
				
			
			}

	//========================== go to jail square ====================================!	

			if (place == 30)
			{
				place = 10;
				
			}
			
			StreetEntity bean = ls.get(place);
			bean.setHits(bean.getHits()+1);
			ls.set(place, bean);
		}
		
		ls.sort(new Comparator<StreetEntity>() {

			@Override
			public int compare(StreetEntity o1, StreetEntity o2) {
				
				return o2.getHits().compareTo(o1.getHits());
			}
			
		});
		
		System.out.println("Hits per 1000000 dice rolls in Monopoly:\n");
		for(StreetEntity ent : ls)
		{
			System.out.println("Place: "+ent.getKey()+" Street: "+ ent.getValue()+" Hits: "+ent.getHits());
		}
	}
	
	/**
	 * This function read a JSON file with key as place, value as street name and hits as a counter
	 * @return List<StreetEntity>
	 */
	private static List<StreetEntity> readJSON()
	{
		
		List<StreetEntity> ls = new ArrayList<StreetEntity>();
		try {
			ClassLoader cl = Monopoly.class.getClassLoader();
			InputStream istr = cl.getResourceAsStream("monopolySt.json");
			ObjectMapper mapper = new ObjectMapper();
			ls = mapper.readValue(istr, new TypeReference<List<StreetEntity>>() {});
			

			
		} catch (JsonParseException e) {
			
			e.printStackTrace();
		} catch (JsonMappingException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return ls;
	}
	
}
