package ch.julien.query.util;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;


public class FuncsTest {

	private static class TestType {}
	private static class TestSubtype extends TestType {}

	@Test
	public void testTo() {
		// implicitly assert absence of class cast exceptions when assigning to var!
		TestType var;
		
		// same type
		var = Funcs.to(TestType.class).invoke(new TestType());
		assertThat(var).isNotNull();
		
		// subtype
		var = Funcs.to(TestType.class).invoke(new TestSubtype());
		assertThat(var).isNotNull();
		
		// anonymous subtype
		var = Funcs.to(TestType.class).invoke(new TestType() {});
		assertThat(var).isNotNull();
		
		// variable of supertype
		Object testClassInstance = new TestType();
		var = Funcs.to(TestType.class).invoke(testClassInstance);
		assertThat(var).isNotNull();
	}
	
	@Test(expected=ClassCastException.class)
	public void testTo_Exception1() {
		// supertype
		@SuppressWarnings("unused")	// no exception without assignment
		TestType var = Funcs.to(TestType.class).invoke(new Object());
	}
	
	@Test(expected=ClassCastException.class)
	public void testTo_Exception2() {
		// unrelated type
		@SuppressWarnings("unused")	// no exception without assignment
		TestType var = Funcs.to(TestType.class).invoke("instance of unrelated type");
	}
	
}
