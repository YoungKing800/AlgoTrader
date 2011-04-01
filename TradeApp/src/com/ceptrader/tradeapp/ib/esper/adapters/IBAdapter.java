package com.ceptrader.tradeapp.ib.esper.adapters;

import com.ceptrader.esper.CEPMan;
import com.ceptrader.tradeapp.ib.esper.events.AccountDownloadEnd;
import com.ceptrader.tradeapp.ib.esper.events.BondContractDetails;
import com.ceptrader.tradeapp.ib.esper.events.ConnectionClosed;
import com.ceptrader.tradeapp.ib.esper.events.ContractDetailsCommon;
import com.ceptrader.tradeapp.ib.esper.events.ContractDetailsEnd;
import com.ceptrader.tradeapp.ib.esper.events.CurrentTime;
import com.ceptrader.tradeapp.ib.esper.events.DeltaNeutralValidation;
import com.ceptrader.tradeapp.ib.esper.events.ExecDetails;
import com.ceptrader.tradeapp.ib.esper.events.ExecDetailsEnd;
import com.ceptrader.tradeapp.ib.esper.events.FundamentalData;
import com.ceptrader.tradeapp.ib.esper.events.HistoricalData;
import com.ceptrader.tradeapp.ib.esper.events.IBErrors;
import com.ceptrader.tradeapp.ib.esper.events.ManagedAccounts;
import com.ceptrader.tradeapp.ib.esper.events.NextValidId;
import com.ceptrader.tradeapp.ib.esper.events.OpenOrder;
import com.ceptrader.tradeapp.ib.esper.events.OpenOrderEnd;
import com.ceptrader.tradeapp.ib.esper.events.OrderStatus;
import com.ceptrader.tradeapp.ib.esper.events.RealtimeBar;
import com.ceptrader.tradeapp.ib.esper.events.ReceiveFA;
import com.ceptrader.tradeapp.ib.esper.events.ScannerData;
import com.ceptrader.tradeapp.ib.esper.events.ScannerDataEnd;
import com.ceptrader.tradeapp.ib.esper.events.ScannerParameters;
import com.ceptrader.tradeapp.ib.esper.events.TickEFP;
import com.ceptrader.tradeapp.ib.esper.events.TickGeneric;
import com.ceptrader.tradeapp.ib.esper.events.TickOptionComputation;
import com.ceptrader.tradeapp.ib.esper.events.TickPrice;
import com.ceptrader.tradeapp.ib.esper.events.TickSize;
import com.ceptrader.tradeapp.ib.esper.events.TickSnapshotEnd;
import com.ceptrader.tradeapp.ib.esper.events.TickString;
import com.ceptrader.tradeapp.ib.esper.events.UpdateAccountTime;
import com.ceptrader.tradeapp.ib.esper.events.UpdateAccountValue;
import com.ceptrader.tradeapp.ib.esper.events.UpdateMktDepth;
import com.ceptrader.tradeapp.ib.esper.events.UpdateMktDepthL2;
import com.ceptrader.tradeapp.ib.esper.events.UpdateNewsBulletin;
import com.ceptrader.tradeapp.ib.esper.events.UpdatePortfolio;
import com.ceptrader.tradeapp.util.Loggable;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.UnderComp;

public class IBAdapter implements EWrapper, Loggable {
	private static IBClient	 eSoc;
	private static IBAdapter	ibAdp;
	
	public static IBAdapter getAdapter() {
		if (IBAdapter.ibAdp == null) {
			IBAdapter.ibAdp = new IBAdapter();
		}
		
		return IBAdapter.ibAdp;
	}
	
	public static IBClient getClient() {
		return IBAdapter.eSoc;
	}
	
	private IBAdapter() {
		IBAdapter.ibAdp = this;
		IBAdapter.eSoc = IBClient.getIBClient();
		
	}
	
	@Override
	public void accountDownloadEnd(final String accountName) {
		CEPMan.getCEPMan().pumpEvent(new AccountDownloadEnd(accountName));
	}
	
	@Override
	public void bondContractDetails(final int reqId,
	        final ContractDetails contractDetails) {
		CEPMan.getCEPMan().pumpEvent(
		        new BondContractDetails(reqId, contractDetails));
	}
	
	@Override
	public void connectionClosed() {
		CEPMan.getCEPMan().pumpEvent(new ConnectionClosed());
	}
	
	@Override
	public void contractDetails(final int reqId,
	        final ContractDetails contractDetails) {
		CEPMan.getCEPMan().pumpEvent(
		        new ContractDetailsCommon(reqId, contractDetails));
	}
	
	@Override
	public void contractDetailsEnd(final int reqId) {
		CEPMan.getCEPMan().pumpEvent(new ContractDetailsEnd(reqId));
	}
	
	@Override
	public void currentTime(final long time) {
		CEPMan.getCEPMan().pumpEvent(new CurrentTime(time));
	}
	
	@Override
	public void deltaNeutralValidation(final int reqId,
	        final UnderComp underComp) {
		CEPMan.getCEPMan()
		        .pumpEvent(new DeltaNeutralValidation(reqId, underComp));
	}
	
	@Override
	public void error(final Exception e) {
		final IBErrors ex = new IBErrors(new IBErrors.EExp(e));
		CEPMan.getCEPMan().pumpEvent(ex);
	}
	
	@Override
	public void error(final int id, final int errorCode, final String errorMsg) {
		final IBErrors ex = new IBErrors(new IBErrors.ECode(id, errorCode,
		        errorMsg));
		CEPMan.getCEPMan().pumpEvent(ex);
	}
	
	@Override
	public void error(final String str) {
		final IBErrors ex = new IBErrors(new IBErrors.EStr(str));
		CEPMan.getCEPMan().pumpEvent(ex);
	}
	
	@Override
	public void execDetails(final int reqId, final Contract contract,
	        final Execution execution) {
		CEPMan.getCEPMan().pumpEvent(
		        new ExecDetails(reqId, contract, execution));
	}
	
	@Override
	public void execDetailsEnd(final int reqId) {
		CEPMan.getCEPMan().pumpEvent(new ExecDetailsEnd(reqId));
	}
	
	@Override
	public void fundamentalData(final int reqId, final String data) {
		CEPMan.getCEPMan().pumpEvent(new FundamentalData(reqId, data));
	}
	
	@Override
	public void historicalData(final int reqId, final String date,
	        final double open, final double high, final double low,
	        final double close, final int volume, final int count,
	        final double WAP, final boolean hasGaps) {
		CEPMan.getCEPMan().pumpEvent(
		        new HistoricalData(reqId, date, open, high, low, close, volume,
		                count, WAP, hasGaps));
	}
	
	@Override
	public void managedAccounts(final String accountsList) {
		CEPMan.getCEPMan().pumpEvent(new ManagedAccounts(accountsList));
	}
	
	private static int	orderId	= 0;
	
	public synchronized static int getNextValidId() {
		return IBAdapter.orderId;
	}
	
	@Override
	public void nextValidId(final int orderId) {
		IBAdapter.orderId = orderId;
		CEPMan.getCEPMan().pumpEvent(new NextValidId(orderId));
	}
	
	@Override
	public void openOrder(final int orderId, final Contract contract,
	        final Order order, final OrderState orderState) {
		CEPMan.getCEPMan().pumpEvent(
		        new OpenOrder(orderId, contract, order, orderState));
	}
	
	@Override
	public void openOrderEnd() {
		CEPMan.getCEPMan().pumpEvent(new OpenOrderEnd());
	}
	
	@Override
	public void orderStatus(final int orderId, final String status,
	        final int filled, final int remaining, final double avgFillPrice,
	        final int permId, final int parentId, final double lastFillPrice,
	        final int clientId, final String whyHeld) {
		CEPMan.getCEPMan().pumpEvent(
		        new OrderStatus(orderId, status, filled, remaining,
		                avgFillPrice,
		                permId, parentId, lastFillPrice, clientId, whyHeld));
	}
	
	@Override
	public void realtimeBar(final int reqId, final long time,
	        final double open,
	        final double high, final double low, final double close,
	        final long volume, final double wap, final int count) {
		CEPMan.getCEPMan().pumpEvent(
		        new RealtimeBar(reqId, time, open, high, low, close, volume,
		                wap,
		                count));
	}
	
	@Override
	public void receiveFA(final int faDataType, final String xml) {
		CEPMan.getCEPMan().pumpEvent(new ReceiveFA(faDataType, xml));
	}
	
	@Override
	public void scannerData(final int reqId, final int rank,
	        final ContractDetails contractDetails, final String distance,
	        final String benchmark, final String projection,
	        final String legsStr) {
		CEPMan.getCEPMan().pumpEvent(
		        new ScannerData(reqId, rank, contractDetails, distance,
		                benchmark,
		                projection, legsStr));
	}
	
	@Override
	public void scannerDataEnd(final int reqId) {
		CEPMan.getCEPMan().pumpEvent(new ScannerDataEnd(reqId));
	}
	
	@Override
	public void scannerParameters(final String xml) {
		CEPMan.getCEPMan().pumpEvent(new ScannerParameters(xml));
	}
	
	@Override
	public void tickEFP(final int tickerId, final int tickType,
	        final double basisPoints, final String formattedBasisPoints,
	        final double impliedFuture, final int holdDays,
	        final String futureExpiry, final double dividendImpact,
	        final double dividendsToExpiry) {
		CEPMan.getCEPMan().pumpEvent(
		        new TickEFP(tickerId, tickType, basisPoints,
		                formattedBasisPoints,
		                impliedFuture, holdDays, futureExpiry, dividendImpact,
		                dividendsToExpiry));
	}
	
	@Override
	public void tickGeneric(final int tickerId, final int tickType,
	        final double value) {
		CEPMan.getCEPMan()
		        .pumpEvent(new TickGeneric(tickerId, tickType, value));
	}
	
	@Override
	public void tickOptionComputation(final int tickerId, final int field,
	        final double impliedVol, final double delta, final double optPrice,
	        final double pvDividend, final double gamma, final double vega,
	        final double theta, final double undPrice) {
		CEPMan.getCEPMan().pumpEvent(
		        new TickOptionComputation(tickerId, field, impliedVol, delta,
		                optPrice, pvDividend, gamma, vega, theta, undPrice));
	}
	
	@Override
	public void tickPrice(final int tickerId, final int field,
	        final double price, final int canAutoExecute) {
		CEPMan.getCEPMan().pumpEvent(
		        new TickPrice(tickerId, field, price, canAutoExecute));
	}
	
	@Override
	public void tickSize(final int tickerId, final int field, final int size) {
		CEPMan.getCEPMan().pumpEvent(new TickSize(tickerId, field, size));
	}
	
	@Override
	public void tickSnapshotEnd(final int reqId) {
		CEPMan.getCEPMan().pumpEvent(new TickSnapshotEnd(reqId));
	}
	
	@Override
	public void tickString(final int tickerId, final int tickType,
	        final String value) {
		CEPMan.getCEPMan().pumpEvent(new TickString(tickerId, tickType, value));
	}
	
	@Override
	public void updateAccountTime(final String timeStamp) {
		CEPMan.getCEPMan().pumpEvent(new UpdateAccountTime(timeStamp));
	}
	
	@Override
	public void updateAccountValue(final String key, final String value,
	        final String currency, final String accountName) {
		CEPMan.getCEPMan().pumpEvent(
		        new UpdateAccountValue(key, value, currency, accountName));
	}
	
	@Override
	public void updateMktDepth(final int tickerId, final int position,
	        final int operation, final int side, final double price,
	        final int size) {
		CEPMan.getCEPMan()
		        .pumpEvent(
		                new UpdateMktDepth(tickerId, position, operation, side,
		                        price, size));
	}
	
	@Override
	public void updateMktDepthL2(final int tickerId, final int position,
	        final String marketMaker, final int operation, final int side,
	        final double price, final int size) {
		CEPMan.getCEPMan().pumpEvent(
		        new UpdateMktDepthL2(tickerId, position, marketMaker,
		                operation,
		                side, price, size));
	}
	
	@Override
	public void updateNewsBulletin(final int msgId, final int msgType,
	        final String message, final String origExchange) {
		CEPMan.getCEPMan().pumpEvent(
		        new UpdateNewsBulletin(msgId, msgType, message, origExchange));
	}
	
	@Override
	public void updatePortfolio(final Contract contract, final int position,
	        final double marketPrice, final double marketValue,
	        final double averageCost, final double unrealizedPNL,
	        final double realizedPNL, final String accountName) {
		CEPMan.getCEPMan().pumpEvent(
		        new UpdatePortfolio(contract, position, marketPrice,
		                marketValue,
		                averageCost, unrealizedPNL, realizedPNL, accountName));
	}
}