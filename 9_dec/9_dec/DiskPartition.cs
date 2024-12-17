using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _9_dec
{
    internal class DiskPartition
    {
        private PartitionType _partitionType;
        private int _id;
        private int _size;

        public PartitionType PartitionType 
        { 
            get { return _partitionType; } 
            set { _partitionType = value; }
        }

        public int Id
        {
            get { return _id; }
        }
        public int Size
        {
            get { return _size; }
            set { _size = value; }
        }

        public DiskPartition(PartitionType partitionType, int id, int size)
        {
            _partitionType = partitionType;
            _id = id;
            _size = size;
        }

        public bool IsEmpty()
        {
            return _size == 0 || _partitionType == PartitionType.EMPTY;
        }
    }
}
