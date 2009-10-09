using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace epZillaEventProcessor.EventProcessorInfrastructure
{
    public class Event
    {
        private string eventId;
        private string sourceId;        
        private string sourceName;
        private string dataItem;
        private string clientId;
        private string clientName;
        private DateTime recievedTime;

        public DateTime RecievedTime
        {
            get
            {
                return recievedTime;
            }
            set
            {
                recievedTime = value;
            }
        }

        public string SourceId
        {
            get
            {
                return sourceId;
            }
            set
            {
                sourceId = value;
            }
        }
        public string ClientId
        {
            get
            {
                return clientId;
            }
            set
            {
                clientId = value;
            }
        }
        

        public string ClientName
        {
            get
            {
                return clientName;
            }
            set
            {
                clientName = value;
            }
        }

        public string EventId
        {
            get
            {
                return eventId;
            }
            set
            {
                eventId = value;
            }
        }
        

        public string SourceName
        {
            get
            {
                return sourceName;
            }
            set
            {
                sourceName = value;
            }
        }
        

        public string DataItem
        {
            get
            {
                return dataItem;
            }
            set
            {
                dataItem = value;
            }
        }




    }
}
