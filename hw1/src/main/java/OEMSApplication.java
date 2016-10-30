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

//***************************************************************************************************************************************************




//***************************************************************************************************************************************************

public class OEMSApplication implements Application
{
  //=================================================================================================================================================

  public String name  ;

  //=================================================================================================================================================

  public OEMSApplication ( String aName )
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
  }

  //=================================================================================================================================================
}

//***************************************************************************************************************************************************

