
public class Result {

	private int exitVal;
	private String sysOut;
	private String errOut;

	public Result(int exitVal, String outPut) {
		this.exitVal=exitVal;
		if (exitVal>0) {
			this.errOut=outPut;
			this.sysOut="";
		} else {
			this.sysOut=outPut;
			this.errOut="";
		}
	}

	public Result(int exitValue, String sysOut, String errOut) {
		this.exitVal=exitValue;
		this.errOut=errOut;
		this.sysOut=sysOut;
	}

	public int getExitVal() {
		return exitVal;
	}

	public String getErrOut() {
		return errOut;
	}

	public String getSysOut() {
		return sysOut;
	}

}
