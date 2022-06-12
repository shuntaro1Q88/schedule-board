package com.shunproduct.scheduleboard.util;

public enum Role {
	
	   ADMIN(0, "管理"),
	   USER(1, "編集"),
	   GUEST(2, "閲覧"),
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
