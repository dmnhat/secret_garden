package com.nng.onedollar.models;

import java.util.ArrayList;

public class ProductModel {

	public int id;
	public String title;
	//TODO: I'm convert 'int' into 'String'
	public String category;
	public String description;
	public int no_of_days;
	public int leverage;
	public boolean is_new;
	public String price;
	public Creator creator;
	public ArrayList<Photo> photos;

	public class Creator {

		public int id;
		public String uri;
		public String email;
		public String first_name;
		public String last_name;
		public int gender;
		public String about;
		public String avatar;
		public int credits;
		public String relationship_status;
		public String dob;
	}

	public class Photo {

		public String image;
	}

}
