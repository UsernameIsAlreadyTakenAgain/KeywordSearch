
public class FK {

	 private final String table;//this table
	 private final	String column;//this table's column who has FK
	 private final	String rtable;//referenced table
	 private final	String rcolumn;//referenced column
	 
		public FK(String table,String column,String rtable,String rcolumn)
		{
			this.table=table;
			this.column=column;
			this.rtable=rtable;
			this.rcolumn=rcolumn;
		}
		
		public String getTable(){
			return table;
		}
		
		public String getColumn(){
			return column;
		}
		
		public String getRtable(){
			return rtable;
		}
		
		public String getRcolumn(){
			return rcolumn;
		}
}
