using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using epZillaEventProcessor.EventProcessorEngine;

namespace epZillaParser
{
    class Program
    {
        static void Main ( string [ ] args )
        {
            Console.WriteLine ( "Hello world... This is epZilla" );
            Console.WriteLine ( "Created by project epZilla group from UoM" );

            Engine engine = new Engine ( );

            Console.Read ( );
        }
    }
}
