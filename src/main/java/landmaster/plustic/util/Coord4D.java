package landmaster.plustic.util;

import java.util.*;

import io.netty.buffer.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.common.*;

public class Coord4D {
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int dimensionId;
	
	public Coord4D(Entity ent) {
		xCoord = (int)ent.posX;
		yCoord = (int)ent.posY;
		zCoord = (int)ent.posZ;
		
		dimensionId = ent.dimension;
	}
	
	public Coord4D(double x, double y, double z, int dimension) {
		xCoord = MathHelper.floor(x);
		yCoord = MathHelper.floor(y);
		zCoord = MathHelper.floor(z);
		dimensionId = dimension;
	}
	
	public Coord4D(BlockPos pos, World world) {
		this(pos.getX(), pos.getY(), pos.getZ(), world.provider.getDimension());
	}
	
	public static Coord4D fromNBT(NBTTagCompound nbt) {
		if (nbt.getSize() == 0) return null;
		return new Coord4D(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"), nbt.getInteger("dim"));
	}
	
	public NBTTagCompound toNBT(NBTTagCompound nbt) {
		nbt.setInteger("x", xCoord);
		nbt.setInteger("y", yCoord);
		nbt.setInteger("z", zCoord);
		nbt.setInteger("dim", dimensionId);
		return nbt;
	}
	
	public static Coord4D fromByteBuf(ByteBuf bb) {
		return new Coord4D(bb.readInt(), bb.readInt(), bb.readInt(), bb.readInt());
	}
	
	public ByteBuf toByteBuf(ByteBuf bb) {
		return bb.writeInt(xCoord).writeInt(yCoord).writeInt(zCoord).writeInt(dimensionId);
	}
	
	public Coord4D add(int x, int y, int z) {
		return new Coord4D(xCoord+x, yCoord+y, zCoord+z, dimensionId);
	}
	
	public IBlockState blockState() {
		WorldServer world = world();
		if (world == null) return null;
		return world.getBlockState(pos());
	}
	
	public TileEntity TE() {
		WorldServer world = world();
		if (world == null) return null;
		return world.getTileEntity(pos());
	}
	
	public BlockPos pos() {
		return new BlockPos(xCoord, yCoord, zCoord);
	}
	
	public WorldServer world() {
		return DimensionManager.getWorld(dimensionId);
	}
	
	@Override
	public String toString() {
		return String.format(Locale.US, "[x=%d, y=%d, z=%d] @ dimension %d", xCoord, yCoord, zCoord, dimensionId);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Coord4D &&
				((Coord4D)obj).xCoord == xCoord &&
				((Coord4D)obj).yCoord == yCoord &&
				((Coord4D)obj).zCoord == zCoord &&
				((Coord4D)obj).dimensionId == dimensionId;
	}
}
