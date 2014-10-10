package ebook;

/**
 * Class containing the data of the person, the author or translator
 */
public class Person {
	/**
	 * Last name of the person
	 */
	public String lastName;
	/**
	 * First name of the person
	 */
	public String firstName;
	/**
	 * Middle name of the person
	 */
	public String middleName;
	/**
	 * Nick name of the person
	 */
	public String nickName;

	/**
	 * The class constructor, fills the fields with null values.
	 */
	public Person() {
		lastName = firstName = middleName = nickName= null;
	}

	/**
	 * The class constructor, fills the fields from parsing person
	 * full name
	 * @param name - The person full name
	 */
	public Person(String name) {
		name.trim();
		String[] nameParts = name.split("[\\s]+");
		int count = nameParts.length;
		if (count > 2) {
			this.firstName = nameParts[0];
			this.lastName = nameParts[count - 1];
			this.middleName = "";
			for (int index = 1; index < count - 1; index++) {
				this.middleName += nameParts[index];
			}
		} else if (count == 2) {
			this.firstName = nameParts[0];
			this.lastName = nameParts[count - 1];
		} else if (count == 1) {
			this.lastName = nameParts[0];
		}
	}
}
