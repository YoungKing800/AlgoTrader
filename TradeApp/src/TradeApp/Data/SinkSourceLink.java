package TradeApp.Data;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import TradeApp.CEP.CEPDataSink;
import TradeApp.CEP.CEPDataSource;
import TradeApp.Signal.SignalEventFramework;

public class SinkSourceLink extends SimpleSinkSourceLink implements
	SignalEventFramework, SignalDataSource,
	SignalDataSink {
	public SinkSourceLink(final int buffLen) {
		super(buffLen);
	}
	
	public SinkSourceLink() {
		super(0);
	}
	
	private final Hashtable<String, DataSource>	sources	= new Hashtable<String, DataSource>();
	private final Hashtable<String, DataSink>		sink		= new Hashtable<String, DataSink>();
	private final Hashtable<String, String>		inOutMap	= new Hashtable<String, String>();
	
	@Override
	public void listen(final String... signal) {
		if (sources.contains(signal[0])) throw new IllegalStateException(
					"Already listening to signal: " + signal);
		
		if (signal.length != 2) throw new IllegalArgumentException(
					"Wrong number of arguments: " + Arrays.asList(signal));
		
		final String name = signal[1];
		
		final CEPDataSource s = new CEPDataSource(CEPDataSource.buildStatement(
					signal[0],
					"name",
					name, true));
		
		sources.put(
					signal[0],
					s);
	}
	
	@Override
	public void listen(final String name, final DataSource source) {
		sources.put(name, source);
	}
	
	@Override
	public void regEventNotify(final String name) {
		sink.put(name, new CEPDataSink());
	}
	
	@Override
	public void regEventNotify(final String name, final DataSink sink) {
		this.sink.put(name, sink);
	}
	
	@Override
	public <T extends TSDataItem> List<T> readInputElements(final String sig) {
		final String theSig = inOutMap.get(sig);
		final DataSource ds = theSig == null ? sources.get(sig) : sources
			.get(theSig);
		return (List<T>) (ds == null ? null : ds.readInputElements());
	}
	
	@Override
	public <T extends TSDataItem> void writeOuputElements(final String sig,
				final List<T> items) {
		final DataSink ds = sink.get(sig);
		
		if (ds == null)
			throw new IllegalArgumentException("No registed sink for: " + sig);
		
		ds.writeOuputElements(items);
	}
	
	@Override
	public void map(final String out, final String in) {
		inOutMap.put(out, in);
	}
}