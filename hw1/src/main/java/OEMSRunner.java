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

import java.util.Date                 ;
import java.util.TimeZone             ;

//---------------------------------------------------------------------------------------------------------------------------------------------------

import quickfix.Session               ;
import quickfix.SessionID             ;
import quickfix.ConfigError           ;
import quickfix.Application           ;
import quickfix.FileLogFactory        ;
import quickfix.MessageFactory        ;
import quickfix.SessionSettings       ;
import quickfix.SocketInitiator       ;
import quickfix.FileStoreFactory      ;
import quickfix.DefaultMessageFactory ;

//---------------------------------------------------------------------------------------------------------------------------------------------------

import quickfix.field.Side            ;
import quickfix.field.Price           ;
import quickfix.field.StopPx          ;
import quickfix.field.Symbol          ;
import quickfix.field.ClOrdID         ;
import quickfix.field.OrdType         ;
import quickfix.field.OrderQty        ;
import quickfix.field.HandlInst       ;
import quickfix.field.TimeInForce     ;
import quickfix.field.TransactTime    ;

//---------------------------------------------------------------------------------------------------------------------------------------------------

import quickfix.fix42.NewOrderSingle  ;

//***************************************************************************************************************************************************




//***************************************************************************************************************************************************

public class OEMSRunner
{
  //=================================================================================================================================================

  public Application      oems                 ;
  public SessionSettings  oemsSettings         ;
  public MessageFactory   oemsMessageFactory   ;
  public FileLogFactory   oemsFileLogFactory   ;
  public FileStoreFactory oemsFileStoreFactory ;
  public SocketInitiator  oemsSocketInitiator  ;

  //-------------------------------------------------------------------------------------------------------------------------------------------------

  public Session          session              ;

  //=================================================================================================================================================

  public OEMSRunner ( String settingsFileName ) throws Exception
  {
    //-----------------------------------------------------------------------------------------------------------------------------------------------

    System.out.println( "OEMS Runner: constructor" ) ;

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    oems                 = new OEMSApplication      ( "OEMS"                                                                               ) ;
    oemsSettings         = new SessionSettings      ( settingsFileName                                                                     ) ;
    oemsMessageFactory   = new DefaultMessageFactory(                                                                                      ) ;
    oemsFileLogFactory   = new FileLogFactory       ( oemsSettings                                                                         ) ;
    oemsFileStoreFactory = new FileStoreFactory     ( oemsSettings                                                                         ) ;
    oemsSocketInitiator  = new SocketInitiator      ( oems , oemsFileStoreFactory , oemsSettings , oemsFileLogFactory , oemsMessageFactory ) ;

    //-----------------------------------------------------------------------------------------------------------------------------------------------
  }

  //=================================================================================================================================================

  public void start () throws ConfigError
  {
    System.out.println( "OEMS Runner: start" ) ;

    oemsSocketInitiator.start() ;

    SessionID sessionId = oemsSocketInitiator.getSessions().get(0) ;

    session = Session.lookupSession( sessionId ) ;
  }

  //=================================================================================================================================================

  public void stop ()
  {
    System.out.println( "OEMS Runner: stop" ) ;

    oemsSocketInitiator.stop() ;
  }

  //=================================================================================================================================================

  public static void main ( String[] args ) throws Exception
  {
    //-----------------------------------------------------------------------------------------------------------------------------------------------

    TimeZone.setDefault( TimeZone.getTimeZone( "Turkey" ) ) ;

    OEMSRunner oemsRunner = new OEMSRunner( "OEMSSettings.txt" ) ;

    oemsRunner.start() ;

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    oemsRunner.session.logon() ;

    try                               { Thread.sleep( 5000 ) ; }
    catch ( InterruptedException e )  {                        }

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    NewOrderSingle message = new NewOrderSingle() ;

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    ClOrdID      fClOrdID      = new ClOrdID     ( "OEMS-Order0001-30.10.2015-13:59:00"       ) ;  /*               Required */
    HandlInst    fHandlInst    = new HandlInst   ( HandlInst.AUTOMATED_EXECUTION_ORDER_PUBLIC ) ;  /*               Required */
    Symbol       fSymbol       = new Symbol      ( "IBM"                                      ) ;  /*               Required */
    Side         fSide         = new Side        ( Side.BUY                                   ) ;  /*               Required */
    TransactTime fTransactTime = new TransactTime( new Date()                                 ) ;  /*               Required */
    OrderQty     fOrderQty     = new OrderQty    ( 500.0                                      ) ;  /* Conditionally Required */
    OrdType      fOrdType      = new OrdType     ( OrdType.LIMIT                              ) ;  /*               Required */
    Price        fPrice        = new Price       ( 3.95                                       ) ;  /* Conditionally Required */
    StopPx       fStopPx       = new StopPx      ( 3.50                                       ) ;  /* Conditionally Required */
    TimeInForce  fTimeInForce  = new TimeInForce ( TimeInForce.DAY                            ) ;

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    message.set( fClOrdID      ) ;
    message.set( fHandlInst    ) ;
    message.set( fSymbol       ) ;
    message.set( fSide         ) ;
    message.set( fTransactTime ) ;
    message.set( fOrderQty     ) ;
    message.set( fOrdType      ) ;
    message.set( fPrice        ) ;
    message.set( fStopPx       ) ;
    message.set( fTimeInForce  ) ;

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    oemsRunner.session.send( message ) ;

    try                               { Thread.sleep( 1000 ) ; }
    catch ( InterruptedException e )  {                        }

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    oemsRunner.session.logout() ;

    try                               { Thread.sleep( 1000 ) ; }
    catch ( InterruptedException e )  {                        }

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    oemsRunner.stop() ;

    //-----------------------------------------------------------------------------------------------------------------------------------------------
  }

  //=================================================================================================================================================
}

//***************************************************************************************************************************************************

