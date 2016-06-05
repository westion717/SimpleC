package zeson.simpleC;

import org.junit.Assume;

public abstract class Value {
	public abstract Type getValueType();

	public abstract Value dup();

	public Value add(Value v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	public Value sub(Value v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	public Value mul(Value v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	public Value div(Value v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	public Value assign(Value v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	public Value less(Value v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	public Value greater(Value v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	public Value eq(Value v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	protected Value added(IntegerValue v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	protected Value subed(IntegerValue v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	protected Value muled(IntegerValue v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	protected Value dived(IntegerValue v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	protected Value assigned(IntegerValue v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	protected Value lessed(IntegerValue v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	protected Value greatered(IntegerValue v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

	protected Value eqed(IntegerValue v) {
		// can not go here
		Assume.assumeTrue(false);
		return null;
	}

}

class BooleanValue extends Value {
	boolean val;

	public BooleanValue(boolean val) {
		super();
		this.val = val;
	}

	@Override
	public Type getValueType() {

		return Type.Bool;
	}

	@Override
	public Value dup() {
		return new BooleanValue(this.val);
	}

}

class StringValue extends Value {
	String val;

	public StringValue(String val) {
		super();
		this.val = val;
	}

	@Override
	public Type getValueType() {

		return Type.String;
	}

	@Override
	public String toString() {
		return val;
	}

	@Override
	public Value dup() {
		return new StringValue(this.val);
	}

}

class VoidValue extends Value {

	public VoidValue() {
		super();
	}

	@Override
	public Type getValueType() {

		return Type.Void;
	}

	@Override
	public Value dup() {
		return new VoidValue();
	}

}

class IntegerValue extends Value {
	int val;
	boolean isInital = false;

	public IntegerValue(int val) {
		super();
		this.val = val;
		this.isInital = true;
	}

	public IntegerValue() {

	}

	@Override
	public Type getValueType() {

		return Type.Int;
	}

	@Override
	public Value add(Value v) {
		return v.added(this);
	}

	@Override
	public Value sub(Value v) {
		return v.subed(this);
	}

	@Override
	public Value mul(Value v) {
		return v.muled(this);
	}

	@Override
	public Value div(Value v) {
		return v.dived(this);
	}

	@Override
	public Value assign(Value v) {

		return v.assigned(this);
	}

	@Override
	public Value less(Value v) {
		return v.lessed(this);
	}

	@Override
	public Value greater(Value v) {
		return v.greatered(this);
	}

	@Override
	public Value eq(Value v) {
		return v.eqed(this);
	}

	@Override
	protected Value added(IntegerValue v) {

		return new IntegerValue(v.val + this.val);
	}

	@Override
	protected Value subed(IntegerValue v) {
		return new IntegerValue(v.val - this.val);
	}

	@Override
	protected Value muled(IntegerValue v) {
		return new IntegerValue(v.val * this.val);
	}

	@Override
	protected Value dived(IntegerValue v) {

		return new IntegerValue(v.val / this.val);
	}

	@Override
	protected Value assigned(IntegerValue v) {
		v.val = this.val;
		return null;
	}

	@Override
	protected Value lessed(IntegerValue v) {

		return new BooleanValue(v.val < this.val);
	}

	@Override
	protected Value greatered(IntegerValue v) {
		return new BooleanValue(v.val > this.val);
	}

	@Override
	protected Value eqed(IntegerValue v) {
		return new BooleanValue(v.val == this.val);
	}

	@Override
	public String toString() {
		return val + "";
	}

	@Override
	public Value dup() {
		return new IntegerValue(this.val);
	}

}
