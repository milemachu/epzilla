using System;
namespace epZillaShared
{
   public interface IEvent
    {
        string ClientId
        {
            get;
            set;
        }
        string ClientName
        {
            get;
            set;
        }
        string DataItem
        {
            get;
            set;
        }
        string EventId
        {
            get;
            set;
        }
        DateTime RecievedTime
        {
            get;
            set;
        }
        string SourceId
        {
            get;
            set;
        }
        string SourceName
        {
            get;
            set;
        }
    }
}
