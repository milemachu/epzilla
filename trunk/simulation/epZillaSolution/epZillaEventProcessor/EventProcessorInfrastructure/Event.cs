using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using epZillaShared;

namespace epZillaEventProcessor.EventProcessorInfrastructure
{
    public class Event : IEvent
    {





        #region IEvent Members

        public string ClientId
        {
            get
            {
                return this.ClientId;
            }
            set
            {
                this.ClientId=value;
            }
        }

        public string ClientName
        {
            get
            {
                return this.ClientName;
            }
            set
            {
                this.ClientName=value;
            }
        }

        public string DataItem
        {
            get
            {
               return this.DataItem;
            }
            set
            {
               this.DataItem=value;
            }
        }

        public string EventId
        {
            get
            {
                return this.EventId;
            }
            set
            {
                this.EventId=value;
            }
        }

        public DateTime RecievedTime
        {
            get
            {
                return this.RecievedTime;
            }
            set
            {
                this.RecievedTime=value;
            }
        }

        public string SourceId
        {
            get
            {
                return this.SourceId;
            }
            set
            {
                this.SourceId=value;
            }
        }

        public string SourceName
        {
            get
            {
               return this.SourceName;
            }
            set
            {
                this.SourceName=value;
            }
        }

        #endregion
    }
}
