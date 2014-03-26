package d567;

public interface IDebugServer
{
	boolean RegisterDebugProvider(IDebugProvider p);
	void UnregisterDebugProvider(IDebugProvider p);

	
}
