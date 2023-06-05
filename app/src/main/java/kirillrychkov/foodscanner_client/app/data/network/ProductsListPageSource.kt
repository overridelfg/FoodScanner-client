package kirillrychkov.foodscanner_client.app.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kirillrychkov.foodscanner_client.app.data.PrefsStorage
import kirillrychkov.foodscanner_client.app.data.network.models.ProductDTO
import kirillrychkov.foodscanner_client.app.data.network.models.toProduct
import kirillrychkov.foodscanner_client.app.domain.OperationResult
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class ProductsListPageSource(
    val apiService: ServerAPI,
    val prefsStorage: PrefsStorage,
    val query: String,
    val sort: String,
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return withContext(Dispatchers.IO){
            try{
                val page: Int = params.key ?: INITIAL_PAGE_NUMBER
                val pageSize: Int = 10

                val token = "Bearer " + prefsStorage.getUser()!!.accessToken
                var response : Response<List<ProductDTO>>
                if(query.isEmpty()){
                    if(sort == "normal"){
                        response = apiService.getProducts(token)
                    }else{
                        response = apiService.getValidProducts(token)
                    }
                }else{
                    response = apiService.getProductBySearch(token, query, page)
                }
                if (response.isSuccessful && response.body() != null){
                    val result = response.body()!!.map {
                        it.toProduct()
                    }
                    val nextKey = if (result.size < pageSize) null else page + 1
                    val prevKey = if(page == 1) null else page - 1
                    return@withContext LoadResult.Page(result, prevKey, nextKey)
                }
                else if (response.errorBody() != null) {
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    if (errorObj.getString("error") == "Token expired!") {
                        refreshToken()
                        load(params)
                        return@withContext LoadResult.Error(Exception(errorObj.getString("Token expired!")))
                    }else{
                        return@withContext LoadResult.Error(Exception(errorObj.getString("error")))
                    }
                }
                else{
                    return@withContext LoadResult.Error(HttpException(response))
                }

            }catch (e: HttpException) {
                return@withContext LoadResult.Error(e)

            }
            catch (e: IOException) {
                return@withContext LoadResult.Error(e)
            }catch (e: Exception) {
                return@withContext LoadResult.Error(e)
            }
        }
    }


    companion object {
        const val INITIAL_PAGE_NUMBER = 1
    }

    private suspend fun refreshToken(){
        withContext(Dispatchers.IO) {
            var refreshToken = ""
            synchronized(this){
                refreshToken = prefsStorage.getUser()!!.refreshToken
            }
            val result = apiService.refreshAccessToken(refreshToken).body()!!
            prefsStorage.refreshTokens(result)
        }
    }
}