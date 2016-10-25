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

import java.util.TimeZone              ;

//---------------------------------------------------------------------------------------------------------------------------------------------------

import quickfix.Application            ;
import quickfix.ConfigError            ;
import quickfix.FileLogFactory         ;
import quickfix.MessageFactory         ;
import quickfix.SessionSettings        ;
import quickfix.FileStoreFactory       ;
import quickfix.DefaultMessageFactory  ;
import quickfix.ThreadedSocketAcceptor ;

//***************************************************************************************************************************************************




//***************************************************************************************************************************************************

public class MarketRunner
{
  //=================================================================================================================================================

  public Application            market                 ;
  public SessionSettings        marketSettings         ;
  public MessageFactory         marketMessageFactory   ;
  public FileLogFactory         marketFileLogFactory   ;
  public FileStoreFactory       marketFileStoreFactory ;
  public ThreadedSocketAcceptor marketSocketAcceptor   ;

  //=================================================================================================================================================

  public MarketRunner ( String settingsFileName ) throws Exception
  {
    //-----------------------------------------------------------------------------------------------------------------------------------------------

    System.out.println( "Market Runner: constructor" ) ;

    //-----------------------------------------------------------------------------------------------------------------------------------------------

    market                 = new MarketApplication     ( "MARKET"                                                                                   );
    marketSettings         = new SessionSettings       ( settingsFileName                                                                           );
    marketMessageFactory   = new DefaultMessageFactory (                                                                                            );
    marketFileLogFactory   = new FileLogFactory        ( marketSettings                                                                             );
    marketFileStoreFactory = new FileStoreFactory      ( marketSettings                                                                             );
    marketSocketAcceptor   = new ThreadedSocketAcceptor( market, marketFileStoreFactory, marketSettings, marketFileLogFactory, marketMessageFactory );

    //-----------------------------------------------------------------------------------------------------------------------------------------------
  }

  //=================================================================================================================================================

  public void start () throws ConfigError
  {
    System.out.println( "Market Runner: start" ) ;

    marketSocketAcceptor.start() ;
  }

  //=================================================================================================================================================

  public void stop ()
  {
    System.out.println( "Market Runner: stop" ) ;

    marketSocketAcceptor.stop() ;
  }

  //=================================================================================================================================================

  public static void main ( String[] args ) throws Exception
  {
    TimeZone.setDefault( TimeZone.getTimeZone( "Turkey" ) ) ;

    MarketRunner marketRunner = new MarketRunner( "MarketSettings.txt" ) ;

    marketRunner.start() ;
  }

  //=================================================================================================================================================
}

//***************************************************************************************************************************************************

