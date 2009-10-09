using System;
namespace epZillaShared
{
  public  interface ITrigger
    {
        DateTime RecievedTime
        {
            get;
            set;
        }
        string TriggerData
        {
            get;
            set;
        }
        string TriggerId
        {
            get;
            set;
        }
        string TriggerSource
        {
            get;
            set;
        }
    }
}
