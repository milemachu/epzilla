using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using epZillaEventProcessor.TriggerBase;
using epZillaEventProcessor.EventProcessorInfrastructure;

namespace epZillaEventProcessor.EventProcessorEngine
{
    /// <summary>
    /// This is the event Processing Engine.
    /// This is singke threaded and can process only single dosument at a time.
    /// Events can not be queued while processing events. 
    /// Trigger can be added if the processing is not happenning.  
    /// </summary>
    public class Engine
    {
        private List<Trigger> triggerBase;
        private List<Event> eventStream;
        private bool isProcessing;
        private bool isUpdatingTriggerBase;
        private bool isUpdatingEventStream;

       

        public Engine ( )
        {
            triggerBase = new List<Trigger> ( );
            eventStream = new List<Event> ( );

            isProcessing = false;
            isUpdatingTriggerBase = false;
            isUpdatingEventStream = false;
            
            //TEST
            Console.Write ( "Event Processing Engine Started & Initialized." );
        }

        public void insertTrigger ( Trigger newTigger )
        {
            if ( !isProcessing && newTigger!=null)
            {
                isUpdatingTriggerBase = true;
                triggerBase.Add ( newTigger );
                isUpdatingTriggerBase = false;
            }
        }

        public bool removeTrigger ( Trigger trigger )
        {
            if ( !isProcessing && trigger != null )
            {
                isUpdatingTriggerBase = true;
                triggerBase.Remove ( trigger );
                isUpdatingTriggerBase = false;

                return true;
            }

            return false;
        }

        public void insertEvent ( Event newEvent )
        {
            if ( !isProcessing && newEvent != null )
            {
                isUpdatingEventStream = true;
                eventStream.Add ( newEvent );
                isUpdatingEventStream = false;
            } 
        }

        public bool removeEvent ( Event oldEvent )
        {
            if ( !isProcessing && oldEvent != null )
            {
                isUpdatingEventStream = true;
                eventStream.Remove ( oldEvent );
                isUpdatingEventStream = false;
                return true;
            }

            return false;
        }


    }
}
