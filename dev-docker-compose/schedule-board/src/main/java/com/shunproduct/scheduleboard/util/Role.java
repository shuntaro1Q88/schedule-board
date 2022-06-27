package com.shunproduct.scheduleboard.util;

public enum Role {
	
	   ADMIN(0, "admin"),
	   USER(1, "user"),
	   GUEST(2, "guest"),
	   ;

	   private final int value;

	   private final String roleName;

	   private Role(int value, String roleName) {
	       this.value = value;
	       this.roleName = roleName;
	   }

	   public int getValue() {
	       return this.value;
	   }

	   public String getRoleName() {
	       return this.roleName;
	   }

}
