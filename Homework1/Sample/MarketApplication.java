//***************************************************************************************************************************************************
//
// Copyright (C) 2015 Selim Temizer.
//
// This file is part of CENG785Template.
//
// CENG785Template is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
//
// CENG785Template is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License along with CENG785Template. If not, see <http://www.gnu.org/licenses/>.
//
//***************************************************************************************************************************************************




//***************************************************************************************************************************************************

import quickfix.Message                ;
import quickfix.DoNotSend              ;
import quickfix.SessionID              ;
import quickfix.Application            ;
import quickfix.RejectLogon            ;
import quickfix.FieldNotFound          ;
import quickfix.IncorrectTagValue      ;
import quickfix.IncorrectDataFormat    ;
import quickfix.UnsupportedMessageType ;

//---------------------------------------------------------------------------------------------------------------------------------------------------

import quickfix.field.Side             ;
import quickfix.field.Price            ;
import quickfix.field.StopPx           ;
import quickfix.field.Symbol           ;
import quickfix.field.ClOrdID          ;
import quickfix.field.OrdType          ;
import quickfix.field.OrderQty         ;
import quickfix.field.TimeInForce      ;
import quickfix.field.TransactTime     ;

//---------------------------------------------------------------------------------------------------------------------------------------------------

import quickfix.fix42.MessageCracker   ;

//***************************************************************************************************************************************************




//***************************************************************************************************************************************************

public class MarketApplication extends MessageCracker implements Application
{
  //=================================================================================================================================================

  public String name  ;

  //=================================================================================================================================================

  public MarketApplication ( String aName )
  {
    name = aName ;

    System.out.println( name + " : constructor" ) ;
  }

  //=================================================================================================================================================

  @Override
  public void onCreate ( SessionID sessionId )
  {
    System.out.println( name + " : onCreate"         ) ;
    System.out.println( "  SessionID : " + sessionId ) ;
  }

  //=================================================================================================================================================

  @Override
  public void onLogon ( SessionID sessionId )
  {
    System.out.println( name + " : onLogon"          ) ;
    System.out.println( "  SessionID : " + sessionId ) ;
  }

  //=================================================================================================================================================

  @Override
  public void onLogout ( SessionID sessionId )
  {
    System.out.println( name + " : onLogout"         ) ;
    System.out.println( "  SessionID : " + sessionId ) ;
  }

  //=================================================================================================================================================

  @Override
  public void toAdmin ( Message message , SessionID sessionId )
  {
    System.out.println( name + " : toAdmin"          ) ;
    System.out.println( "  SessionID : " + sessionId ) ;
    System.out.println( "  Message   : " + message   ) ;
  }

  //=================================================================================================================================================

  @Override
  public void fromAdmin ( Message message , SessionID sessionId ) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon
  {
    System.out.println( name + " : fromAdmin"        ) ;
    System.out.println( "  SessionID : " + sessionId ) ;
    System.out.println( "  Message   : " + message   ) ;
  }

  //=================================================================================================================================================

  @Override
  public void toApp ( Message message , SessionID sessionId ) throws DoNotSend
  {
    System.out.println( name + " : toApp"            ) ;
    System.out.println( "  SessionID : " + sessionId ) ;
    System.out.println( "  Message   : " + message   ) ;
  }

  //=================================================================================================================================================

  @Override
  public void fromApp ( Message message , SessionID sessionId ) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType
  {
    System.out.println( name + " : fromApp"          ) ;
    System.out.println( "  SessionID : " + sessionId ) ;
    System.out.println( "  Message   : " + message   ) ;

    crack( message , sessionId ) ;
  }

  //=================================================================================================================================================

  @Override
  public void onMessage ( quickfix.fix42.MarketDataRequest message , SessionID sessionId )
  {
    System.out.println( name + " : MarketDataRequest" ) ;
  }

  //=================================================================================================================================================

  @Override
  public void onMessage ( quickfix.fix42.NewOrderSingle message , SessionID sessionId ) throws FieldNotFound
  {
    //-----------------------------------------------------------------------------------------------------------------------------------------------

    System.out.println( name + " : NewOrderSingle" ) ;

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    ClOrdID      fClOrdID      = new ClOrdID     () ;
    Symbol       fSymbol       = new Symbol      () ;
    Side         fSide         = new Side        () ;
    TransactTime fTransactTime = new TransactTime() ;
    OrderQty     fOrderQty     = new OrderQty    () ;
    OrdType      fOrdType      = new OrdType     () ;
    Price        fPrice        = new Price       () ;
    StopPx       fStopPx       = new StopPx      () ;
    TimeInForce  fTimeInForce  = new TimeInForce () ;

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    message.get( fClOrdID      ) ;
    message.get( fSymbol       ) ;
    message.get( fSide         ) ;
    message.get( fTransactTime ) ;
    message.get( fOrderQty     ) ;
    message.get( fOrdType      ) ;
    message.get( fPrice        ) ;
    message.get( fStopPx       ) ;
    message.get( fTimeInForce  ) ;

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    System.out.println( fClOrdID     .toString() ) ;
    System.out.println( fSymbol      .toString() ) ;
    System.out.println( fSide        .toString() ) ;
    System.out.println( fTransactTime.toString() ) ;
    System.out.println( fOrderQty    .toString() ) ;
    System.out.println( fOrdType     .toString() ) ;
    System.out.println( fPrice       .toString() ) ;
    System.out.println( fStopPx      .toString() ) ;
    System.out.println( fTimeInForce .toString() ) ;

    //-----------------------------------------------------------------------------------------------------------------------------------------------
  }

  //=================================================================================================================================================

  @Override
  public void onMessage ( quickfix.fix42.OrderStatusRequest message , SessionID sessionId )
  {
    System.out.println( name + " : OrderStatusRequest" ) ;
  }

  //=================================================================================================================================================

  @Override
  public void onMessage ( quickfix.fix42.OrderCancelRequest message , SessionID sessionId )
  {
    System.out.println( name + " : OrderCancelRequest" ) ;
  }

  //=================================================================================================================================================

  @Override
  public void onMessage ( quickfix.fix42.OrderCancelReplaceRequest message , SessionID sessionId )
  {
    System.out.println( name + " : OrderCancelReplaceRequest" ) ;
  }

  //=================================================================================================================================================
}

//***************************************************************************************************************************************************

