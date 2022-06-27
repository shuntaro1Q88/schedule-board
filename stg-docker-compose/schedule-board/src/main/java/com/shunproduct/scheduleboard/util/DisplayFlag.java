package com.shunproduct.scheduleboard.util;

public enum DisplayFlag {

	TRUE(true, "表示"),
	FALSE(false, "非表示");

	   private final Boolean value;

	   private final String viewName;

	   private DisplayFlag(Boolean value, String viewName) {
	       this.value = value;
	       this.viewName = viewName;
	   }

	   public Boolean getValue() {
	       return this.value;
	   }

	   public String getViewName() {
	       return this.viewName;
	   }

}
